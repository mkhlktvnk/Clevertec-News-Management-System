package ru.clevertec.newsresource.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsresource.entity.Comment;
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
        newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, newsId)
                ));

        Specification<Comment> searchSpecification =
                Specification.where(CommentSpecifications.hasTextLike(criteria.getText()));

        return commentRepository.findAllByNewsId(newsId, searchSpecification, pageable);
    }

    @Override
    public Comment findAllById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, commentId)
                ));
    }

    @Override
    @Transactional
    public Comment insertComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
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
    public void deleteCommentById(Long commentId) {
        Comment commentToDelete = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, commentId)
                ));
        commentRepository.delete(commentToDelete);
    }
}
