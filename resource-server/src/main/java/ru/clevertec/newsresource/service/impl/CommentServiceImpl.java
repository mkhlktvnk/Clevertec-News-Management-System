package ru.clevertec.newsresource.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exception.handling.starter.exception.ResourceNotFoundException;
import ru.clevertec.logging.annotation.Loggable;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.repository.CommentRepository;
import ru.clevertec.newsresource.repository.NewsRepository;
import ru.clevertec.newsresource.service.CommentService;
import ru.clevertec.newsresource.service.PermissionService;
import ru.clevertec.newsresource.service.message.MessagesSource;
import ru.clevertec.newsresource.service.message.key.CommentMessageKey;
import ru.clevertec.newsresource.service.message.key.NewsMessageKey;
import ru.clevertec.newsresource.specifications.CommentSpecifications;
import ru.clevertec.newsresource.web.mapper.CommentMapper;

import java.util.List;

/**
 * Implements the {@link CommentService} interface and provides methods for
 * retrieving, adding, updating, and deleting comments on news articles.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final MessagesSource messagesSource;
    private final PermissionService<User, Comment> permissionService;
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    /**
     * Retrieves a list of comments on a news article, filtered by a search query
     * and paginated.
     *
     * @param newsId the ID of the news article to retrieve comments for
     * @param pageable the pagination information
     * @param query the search query to filter comments by
     * @return a list of comments on the specified news article, filtered by the
     *         search query and paginated
     * @throws ResourceNotFoundException if the news article with the specified ID
     *                                   cannot be found
     */
    @Override
    @Loggable
    public List<Comment> findAllByNewsIdAndPageableAndQueryMatch(
            Long newsId, Pageable pageable, String query) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, newsId)
                ));

        Specification<Comment> searchSpecification =
                Specification.where(CommentSpecifications.hasNewsId(news.getId()))
                        .and(CommentSpecifications.hasMatchWithQuery(query));

        return commentRepository.findAll(searchSpecification, pageable).getContent();
    }

    /**
     * Retrieves a comment by its ID.
     *
     * @param commentId the ID of the comment to retrieve
     * @return the comment with the specified ID
     * @throws ResourceNotFoundException if the comment with the specified ID cannot
     *                                   be found
     */
    @Override
    @Loggable
    @Cacheable(value = "comment", key = "#commentId")
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, commentId)
                ));
    }

    /**
     * Adds a new comment to a news article.
     *
     * @param newsId the ID of the news article to add the comment to
     * @param comment the comment to add
     * @param user the user adding the comment
     * @return the newly created comment
     * @throws ResourceNotFoundException if the news article with the specified ID
     *                                   cannot be found
     */
    @Override
    @Loggable
    @Transactional
    @CachePut(value = "comment", key = "#result.id")
    public Comment addCommentToNews(Long newsId, Comment comment, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, newsId)
                ));
        comment.setUsername(user.getUsername());
        comment.setNews(news);
        return commentRepository.save(comment);
    }

    /**
     * Updates a comment partially by ID.
     *
     * @param commentId the ID of the comment to update
     * @param user the user attempting to update the comment
     * @param updateComment the updated fields of the comment
     * @throws ResourceNotFoundException if the comment with the given ID is not found
     * @throws AccessDeniedException if the user does not have permission to edit the comment
     */
    @Override
    @Loggable
    @Transactional
    @CachePut(value = "comment", key = "#commentId")
    public void updateCommentPartiallyById(Long commentId, User user, Comment updateComment) {
        Comment commentToUpdate = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, commentId)
                ));

        if (!permissionService.userHasPermissionToEditResource(user, commentToUpdate)) {
            throw new AccessDeniedException(messagesSource.get(CommentMessageKey.UNABLE_TO_EDIT));
        }

        commentMapper.mapNotNullFields(commentToUpdate, updateComment);
        commentRepository.save(commentToUpdate);
    }

    /**
     * Deletes a comment by ID if the user has permission to edit it. Throws a ResourceNotFoundException if the comment
     * does not exist, or an AccessDeniedException if the user does not have permission to edit it.
     *
     * @param commentId The ID of the comment to delete
     * @param user      The user requesting the deletion
     * @throws ResourceNotFoundException if the comment with the given ID does not exist
     * @throws AccessDeniedException     if the user does not have permission to delete the comment
     */
    @Override
    @Loggable
    @Transactional
    @CacheEvict(value = "comment", key = "#commentId")
    public void deleteCommentById(Long commentId, User user) {
        Comment commentToDelete = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, commentId)
                ));

        if (!permissionService.userHasPermissionToEditResource(user, commentToDelete)) {
            throw new AccessDeniedException(messagesSource.get(CommentMessageKey.UNABLE_TO_DELETE));
        }

        commentRepository.delete(commentToDelete);
    }
}
