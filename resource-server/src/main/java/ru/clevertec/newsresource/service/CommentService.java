package ru.clevertec.newsresource.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.newsresource.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAllByNewsIdAndPageableAndQueryMatch(Long newsId, Pageable pageable, String query);

    Comment findById(Long commentId);

    Comment addCommentToNews(Long newsId, Comment comment);

    void updateCommentPartiallyById(Long commentId, Comment updateComment);

    void deleteCommentById(Long commentId);
}
