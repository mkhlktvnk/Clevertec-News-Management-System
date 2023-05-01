package ru.clevertec.newsresource.integration.service.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.newsresource.builder.impl.CommentCriteriaTestBuilder;
import ru.clevertec.newsresource.builder.impl.CommentTestBuilder;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.integration.BaseIntegrationTest;
import ru.clevertec.newsresource.service.CommentService;
import ru.clevertec.newsresource.service.exception.ResourceNotFoundException;
import ru.clevertec.newsresource.service.message.MessagesSource;
import ru.clevertec.newsresource.service.message.key.CommentMessageKey;
import ru.clevertec.newsresource.service.message.key.NewsMessageKey;
import ru.clevertec.newsresource.web.criteria.CommentCriteria;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentServiceImplTest extends BaseIntegrationTest {

    public static final Long CORRECT_NEWS_ID = 1L;

    public static final Long INCORRECT_NEWS_ID = 30L;

    public static final Long CORRECT_COMMENT_ID = 2L;

    public static final Long INCORRECT_COMMENT_ID = 40L;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MessagesSource messagesSource;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findAllByNewsIdAndPageableAndCriteriaShouldReturnExpectedCountOfComments() {
        Pageable pageable = PageRequest.of(0, 1);
        CommentCriteria criteria = CommentCriteriaTestBuilder.aCommentCriteria()
                .withUsername("johndoe")
                .withText("great news!")
                .build();

        List<Comment> comments =
                commentService.findAllByNewsIdAndPageableAndCriteria(CORRECT_NEWS_ID, pageable, criteria);

        assertThat(comments.size()).isEqualTo(1);
    }

    @Test
    void findAllByNewsIdAndPageableAndCriteriaShouldThrowResourceNotFoundException() {
        Pageable pageable = PageRequest.of(0, 1);
        CommentCriteria criteria = CommentCriteriaTestBuilder.aCommentCriteria().build();

        assertThatThrownBy(() -> commentService
                .findAllByNewsIdAndPageableAndCriteria(INCORRECT_NEWS_ID, pageable, criteria))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, INCORRECT_NEWS_ID));
    }

    @Test
    void findByIdShouldFindComment() {
        Comment comment = commentService.findById(CORRECT_COMMENT_ID);

        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isEqualTo(CORRECT_COMMENT_ID);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundException() {
        assertThatThrownBy(() -> commentService.findById(INCORRECT_COMMENT_ID))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void insertCommentShouldReturnCommentWithNotNullId() {
        Comment comment = CommentTestBuilder.aComment()
                .withText("New-comment-text")
                .withUsername("User123")
                .build();

        Comment actual = commentService.addCommentToNews(CORRECT_NEWS_ID, comment);

        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void addCommentToNewsShouldThrowResourceNotFoundException() {
        Comment comment = CommentTestBuilder.aComment()
                .withText("New-comment-text")
                .withUsername("User123")
                .build();

        assertThatThrownBy(() -> commentService.addCommentToNews(INCORRECT_NEWS_ID, comment))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, INCORRECT_NEWS_ID));
    }

    @Test
    void updateCommentPartiallyByIdShouldUpdateComment() {
        Comment updateComment = CommentTestBuilder.aComment()
                .withText("new-comment-text").build();

        commentService.updateCommentPartiallyById(CORRECT_COMMENT_ID, updateComment);
        entityManager.flush();
    }

    @Test
    void updateCommentPartiallyByIdShouldThrowResourceNotFoundException() {
        Comment updateComment = CommentTestBuilder.aComment()
                .withText("new-comment-text").build();

        assertThatThrownBy(() -> commentService.updateCommentPartiallyById(INCORRECT_COMMENT_ID, updateComment))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, INCORRECT_COMMENT_ID));
    }

    @Test
    void deleteCommentByIdShouldDeleteComment() {
        commentService.deleteCommentById(CORRECT_COMMENT_ID);
        entityManager.flush();
    }

    @Test
    void deleteCommentByIdShouldThrowResourceNotFoundException() {
        assertThatThrownBy(() -> commentService.deleteCommentById(INCORRECT_COMMENT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, INCORRECT_COMMENT_ID));
    }
}