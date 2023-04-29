package ru.clevertec.newsresource.unit.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.newsresource.web.criteria.CommentCriteria;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.repository.CommentRepository;
import ru.clevertec.newsresource.service.exception.ResourceNotFoundException;
import ru.clevertec.newsresource.service.impl.CommentServiceImpl;
import ru.clevertec.newsresource.service.message.MessagesSource;
import ru.clevertec.newsresource.service.message.key.CommentMessageKey;
import ru.clevertec.newsresource.builder.impl.CommentTestBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    private static final Long COMMENT_ID = 1L;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MessagesSource messagesSource;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void findAllByPageableAndCriteriaShouldReturnExpectedCommentsAndCallRepository() {
        Pageable pageable = PageRequest.of(0, 3);
        CommentCriteria criteria = CommentCriteria.builder().build();
        List<Comment> expectedComments = List.of(
                CommentTestBuilder.aComment().build(),
                CommentTestBuilder.aComment().build(),
                CommentTestBuilder.aComment().build()
        );
        doReturn(new PageImpl<>(expectedComments)).when(commentRepository)
                .findAll(any(Specification.class), any(Pageable.class));

        List<Comment> actualComments = commentService.findAllByPageableAndCriteria(pageable, criteria);

        verify(commentRepository).findAll(any(Specification.class), any(Pageable.class));
        assertThat(actualComments).isEqualTo(expectedComments);
    }

    @Test
    void findCommentByIdShouldReturnExpectedCommentAndCallRepository() {
        Comment expectedComment = CommentTestBuilder.aComment().build();
        doReturn(Optional.of(expectedComment)).when(commentRepository).findById(COMMENT_ID);

        Comment actualComment = commentService.findAllById(COMMENT_ID);

        verify(commentRepository).findById(COMMENT_ID);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    void findCommentByIdShouldThrowResourceNotFoundException() {
        doReturn(Optional.empty()).when(commentRepository).findById(COMMENT_ID);

        assertThatThrownBy(() -> commentService.findAllById(COMMENT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, COMMENT_ID));
    }

    @Test
    void insertCommentShouldReturnSavedCommentAndCallRepository() {
        Comment expectedComment = CommentTestBuilder.aComment().build();
        doReturn(expectedComment).when(commentRepository).save(expectedComment);

        Comment actualComment = commentService.insertComment(expectedComment);

        verify(commentRepository).save(expectedComment);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    void updateCommentPartiallyByIdShouldCallRepository() {
        Comment updateComment = CommentTestBuilder.aComment().build();
        Comment commentFromDb = CommentTestBuilder.aComment().build();
        doReturn(Optional.of(commentFromDb)).when(commentRepository).findById(COMMENT_ID);

        commentService.updateCommentPartiallyById(COMMENT_ID, updateComment);

        verify(commentRepository).findById(COMMENT_ID);
        verify(commentRepository).save(commentFromDb);
    }

    @Test
    void updateCommentPartiallyByIdShouldThrowResourceNotFoundException() {
        Comment updateComment = CommentTestBuilder.aComment().build();
        doReturn(Optional.empty()).when(commentRepository).findById(COMMENT_ID);

        assertThatThrownBy(() -> commentService.updateCommentPartiallyById(COMMENT_ID, updateComment))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(CommentMessageKey.COMMENT_NOT_FOUND_BY_ID, COMMENT_ID));
    }

    @Test
    void deleteCommentByIdShouldCallRepository() {
        Comment commentFromDb = CommentTestBuilder.aComment().build();
        doReturn(Optional.of(commentFromDb)).when(commentRepository).findById(COMMENT_ID);

        commentService.deleteCommentById(COMMENT_ID);

        verify(commentRepository).findById(COMMENT_ID);
        verify(commentRepository).delete(commentFromDb);
    }
}
