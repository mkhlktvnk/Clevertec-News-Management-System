package ru.clevertec.newsresource.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.repository.CommentRepository;
import ru.clevertec.newsresource.repository.NewsRepository;
import ru.clevertec.newsresource.service.CommentService;
import ru.clevertec.newsresource.service.exception.ResourceNotFoundException;
import ru.clevertec.newsresource.service.message.key.CommentMessageKey;
import ru.clevertec.newsresource.service.message.MessagesSource;
import ru.clevertec.newsresource.service.message.key.NewsMessageKey;
import ru.clevertec.newsresource.specifications.CommentSpecifications;
import ru.clevertec.newsresource.web.criteria.CommentCriteria;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final MessagesSource messagesSource;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Comment> findAllByNewsIdAndPageableAndCriteria(
            Long newsId, Pageable pageable, CommentCriteria criteria) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, newsId)
                ));

        Specification<Comment> searchSpecification =
                Specification.where(CommentSpecifications.hasNewsId(news.getId()))
                        .and(CommentSpecifications.hasTextLike(criteria.getText()))
                        .and(CommentSpecifications.hasUsernameLike(criteria.getUsername()));

        return commentRepository.findAll(searchSpecification, pageable).getContent();
    }

    @Override
    @Cacheable(value = "comment", key = "#commentId")
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, commentId)
                ));
    }

    @Override
    @Transactional
    @CachePut(value = "comment", key = "#result.id")
    public Comment addCommentToNews(Long newsId, Comment comment) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, newsId)
                ));
        comment.setNews(news);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    @CachePut(value = "comment", key = "#commentId")
    public void updateCommentPartiallyById(Long commentId, Comment updateComment) {
        Comment commentToUpdate = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, commentId)
                ));
        modelMapper.map(updateComment, commentToUpdate);
        commentRepository.save(commentToUpdate);
    }

    @Override
    @Transactional
    @CacheEvict(value = "comment", key = "#commentId")
    public void deleteCommentById(Long commentId) {
        Comment commentToDelete = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, commentId)
                ));
        commentRepository.delete(commentToDelete);
    }
}
