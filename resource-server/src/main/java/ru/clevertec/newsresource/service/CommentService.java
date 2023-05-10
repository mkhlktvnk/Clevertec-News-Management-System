package ru.clevertec.newsresource.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import ru.clevertec.newsresource.entity.Comment;

import java.util.List;

/**
 * Interface for managing comments on news articles.
 */
public interface CommentService {

    /**
     * Finds all comments for a given news article that match a given query,
     * and returns a pageable result.
     *
     * @param newsId the ID of the news article to find comments for
     * @param pageable the pageable parameters for the result
     * @param query the query to match the comments against
     * @return a pageable list of comments for the news article that match the query
     */
    List<Comment> findAllByNewsIdAndPageableAndQueryMatch(Long newsId, Pageable pageable, String query);

    /**
     * Finds a comment by its ID.
     *
     * @param commentId the ID of the comment to find
     * @return the comment with the given ID, or null if it does not exist
     */
    Comment findById(Long commentId);

    /**
     * Adds a new comment to a news article.
     *
     * @param newsId the ID of the news article to add the comment to
     * @param comment the comment to add
     * @param user the user adding the comment
     * @return the newly added comment
     */
    Comment addCommentToNews(Long newsId, Comment comment, User user);

    /**
     * Partially updates a comment by ID.
     *
     * @param commentId the ID of the comment to update
     * @param user the user updating the comment
     * @param updateComment the updated comment fields
     */
    void updateCommentPartiallyById(Long commentId, User user, Comment updateComment);

    /**
     * Deletes a comment by ID.
     *
     * @param commentId the ID of the comment to delete
     * @param user the user deleting the comment
     */
    void deleteCommentById(Long commentId, User user);
}

