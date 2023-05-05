package ru.clevertec.newsresource.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import ru.clevertec.newsresource.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAllByNewsIdAndPageableAndQueryMatch(Long newsId, Pageable pageable, String query);

    Comment findById(Long commentId);

    Comment addCommentToNews(Long newsId, Comment comment, User user);

    void updateCommentPartiallyById(Long commentId, User user, Comment updateComment);

    void deleteCommentById(Long commentId, User user);
}
