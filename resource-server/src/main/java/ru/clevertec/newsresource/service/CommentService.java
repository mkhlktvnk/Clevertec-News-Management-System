package ru.clevertec.newsresource.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.newsresource.web.criteria.CommentCriteria;
import ru.clevertec.newsresource.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAllByPageableAndCriteria(Pageable pageable, CommentCriteria criteria);

    Comment findAllById(Long commentId);

    Comment insertComment(Comment comment);

    void updateCommentPartiallyById(Long commentId, Comment updateComment);

    void deleteCommentById(Long commentId);
}
