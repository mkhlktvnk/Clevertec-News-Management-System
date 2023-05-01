package ru.clevertec.newsresource.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.newsresource.web.criteria.CommentCriteria;
import ru.clevertec.newsresource.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAllByNewsIdAndPageableAndCriteria(Long newsId, Pageable pageable, CommentCriteria criteria);

    Comment findById(Long commentId);

    Comment addCommentToNews(Long newsId, Comment comment);

    void updateCommentPartiallyById(Long commentId, Comment updateComment);

    void deleteCommentById(Long commentId);
}
