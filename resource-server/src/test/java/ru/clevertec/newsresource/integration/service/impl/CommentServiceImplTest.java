package ru.clevertec.newsresource.integration.service.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import ru.clevertec.exception.handling.starter.exception.ResourceNotFoundException;
import ru.clevertec.newsresource.builder.impl.CommentTestBuilder;
import ru.clevertec.newsresource.builder.impl.UserTestBuilder;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.integration.BaseIntegrationTest;
import ru.clevertec.newsresource.service.CommentService;
import ru.clevertec.newsresource.service.message.MessagesSource;
import ru.clevertec.newsresource.service.message.key.CommentMessageKey;
import ru.clevertec.newsresource.service.message.key.NewsMessageKey;

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
    void findAllByNewsIdAndPageableAndQueryMatchShouldReturnExpectedCountOfComments() {
        Pageable pageable = PageRequest.of(0, 1);
        String query = "I totally agree";

        List<Comment> comments =
                commentService.findAllByNewsIdAndPageableAndQueryMatch(CORRECT_NEWS_ID, pageable, query);

        assertThat(comments.size()).isEqualTo(1);
    }

    @Test
    void findAllByNewsIdAndPageableAndQueryMatchShouldThrowResourceNotFoundException() {
        Pageable pageable = PageRequest.of(0, 1);
        String query = "I'm";

        assertThatThrownBy(() -> commentService
                .findAllByNewsIdAndPageableAndQueryMatch(INCORRECT_NEWS_ID, pageable, query))
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
        User user = UserTestBuilder.anUser().withUsername("user1").build();

        Comment actual = commentService.addCommentToNews(CORRECT_NEWS_ID, comment, user);

        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void addCommentToNewsShouldThrowResourceNotFoundException() {
        Comment comment = CommentTestBuilder.aComment()
                .withText("New-comment-text")
                .withUsername("User123")
                .build();
        User user = UserTestBuilder.anUser().withUsername("user1").build();

        assertThatThrownBy(() -> commentService.addCommentToNews(INCORRECT_NEWS_ID, comment, user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, INCORRECT_NEWS_ID));
    }

    @Test
    void updateCommentPartiallyByIdShouldUpdateComment() {
        Comment updateComment = CommentTestBuilder.aComment()
                .withText("new-comment-text").build();
        User user = UserTestBuilder.anUser()
                .withUsername("user2")
                .build();

        commentService.updateCommentPartiallyById(CORRECT_COMMENT_ID, user, updateComment);
        entityManager.flush();
    }

    @Test
    void updateCommentPartiallyByIdShouldThrowResourceNotFoundException() {
        Comment updateComment = CommentTestBuilder.aComment()
                .withText("new-comment-text").build();
        User user = UserTestBuilder.anUser()
                .withUsername("user2")
                .build();

        assertThatThrownBy(() -> commentService.updateCommentPartiallyById(INCORRECT_COMMENT_ID, user, updateComment))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, INCORRECT_COMMENT_ID));
    }

    @Test
    void deleteCommentByIdShouldDeleteComment() {
        User user = UserTestBuilder.anUser()
                .withUsername("user2")
                .build();

        commentService.deleteCommentById(CORRECT_COMMENT_ID, user);
        entityManager.flush();
    }

    @Test
    void deleteCommentByIdShouldThrowResourceNotFoundException() {
        User user = UserTestBuilder.anUser()
                .withUsername("user2")
                .build();

        assertThatThrownBy(() -> commentService.deleteCommentById(INCORRECT_COMMENT_ID, user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, INCORRECT_COMMENT_ID));
    }
}