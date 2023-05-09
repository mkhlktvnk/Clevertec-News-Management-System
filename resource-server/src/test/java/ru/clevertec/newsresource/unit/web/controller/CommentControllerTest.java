package ru.clevertec.newsresource.unit.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.clevertec.newsresource.builder.impl.CommentDtoTestBuilder;
import ru.clevertec.newsresource.builder.impl.CommentTestBuilder;
import ru.clevertec.newsresource.builder.impl.UserTestBuilder;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.service.CommentService;
import ru.clevertec.newsresource.web.controller.CommentController;
import ru.clevertec.newsresource.web.dto.CommentDto;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    private static final Long NEWS_ID = 1L;

    private static final Long COMMENT_ID = 2L;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @Test
    void findAllByNewsIdAndPageableAndMatchWithQueryShouldReturnCorrectCountOfComments() {
        String query = "query";
        Pageable pageable = PageRequest.of(0, 3);
        List<Comment> comments = List.of(
                CommentTestBuilder.aComment().build(),
                CommentTestBuilder.aComment().build(),
                CommentTestBuilder.aComment().build()
        );
        doReturn(comments).when(commentService)
                .findAllByNewsIdAndPageableAndQueryMatch(NEWS_ID, pageable, query);

        ResponseEntity<List<CommentDto>> response =
                commentController.findAllByNewsIdAndPageableAndMatchWithQuery(NEWS_ID, pageable, query);

        verify(commentService)
                .findAllByNewsIdAndPageableAndQueryMatch(NEWS_ID, pageable, query);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(comments.size());
    }

    @Test
    void findCommentByIdShouldReturnCorrectComment() {
        Comment comment = CommentTestBuilder.aComment()
                .withId(COMMENT_ID)
                .build();
        doReturn(comment).when(commentService).findById(COMMENT_ID);

        ResponseEntity<CommentDto> response = commentController.findCommentById(COMMENT_ID);

        verify(commentService).findById(COMMENT_ID);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(comment.getId());
    }

    @Test
    void addCommentToNewsShouldReturnAddedComment() {
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withRoles(List.of(new SimpleGrantedAuthority("SUBSCRIBER")))
                .build();
        Comment comment = CommentTestBuilder.aComment()
                .withText("New comment")
                .withTime(Instant.now())
                .build();
        CommentDto commentDto = CommentDtoTestBuilder.aCommentDto()
                .withText("New comment")
                .withTime(Instant.now().toString())
                .withNewsId(NEWS_ID)
                .build();
        doReturn(comment).when(commentService)
                .addCommentToNews(any(Long.class), any(Comment.class), any(User.class));

        ResponseEntity<CommentDto> response = commentController.addCommentToNews(commentDto, user);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getText()).isEqualTo(commentDto.getText());
    }

    @Test
    void updateCommentPartiallyByIdShouldCallService() {
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withRoles(List.of(new SimpleGrantedAuthority("SUBSCRIBER")))
                .build();
        CommentDto commentDto = CommentDtoTestBuilder.aCommentDto()
                .withUsername("New comment username")
                .withTime(Instant.now().toString())
                .build();

        commentController.updateCommentPartiallyById(COMMENT_ID, user, commentDto);

        verify(commentService).updateCommentPartiallyById(any(Long.class), any(User.class), any(Comment.class));
    }

    @Test
    void deleteCommentByIdShouldCallService() {
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withRoles(List.of(new SimpleGrantedAuthority("SUBSCRIBER")))
                .build();

        commentController.deleteCommentById(COMMENT_ID, user);

        verify(commentService).deleteCommentById(COMMENT_ID, user);
    }
}