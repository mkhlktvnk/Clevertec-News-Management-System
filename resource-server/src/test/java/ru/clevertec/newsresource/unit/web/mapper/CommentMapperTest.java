package ru.clevertec.newsresource.unit.web.mapper;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import ru.clevertec.newsresource.builder.impl.CommentDtoTestBuilder;
import ru.clevertec.newsresource.builder.impl.CommentTestBuilder;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.web.dto.CommentDto;
import ru.clevertec.newsresource.web.mapper.CommentMapper;

import java.time.Instant;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {

    private final CommentMapper mapper = Mappers.getMapper(CommentMapper.class);

    @ParameterizedTest
    @MethodSource("provideComments")
    void toDtoShouldMapComment(Comment comment) {
        CommentDto commentDto = mapper.toDto(comment);

        assertThat(commentDto.getId()).isEqualTo(comment.getId());
        assertThat(commentDto.getUsername()).isEqualTo(comment.getUsername());
        assertThat(commentDto.getText()).isEqualTo(comment.getText());
        assertThat(commentDto.getTime()).isEqualTo(comment.getTime().toString());
    }

    @ParameterizedTest
    @MethodSource("provideCommentDto")
    void toEntityShouldMapCommentDto(CommentDto commentDto) {
        Comment comment = mapper.toEntity(commentDto);

        assertThat(comment.getId()).isEqualTo(commentDto.getId());
        assertThat(comment.getUsername()).isEqualTo(commentDto.getUsername());
        assertThat(comment.getText()).isEqualTo(commentDto.getText());
        assertThat(comment.getTime()).isEqualTo(commentDto.getTime());
    }

    private static Stream<CommentDto> provideCommentDto() {
        return Stream.of(
                CommentDtoTestBuilder.aCommentDto()
                        .withId(1L)
                        .withUsername("user123")
                        .withText("text-1")
                        .withTime(Instant.now().toString())
                        .build(),
                CommentDtoTestBuilder.aCommentDto()
                        .withId(1L)
                        .withUsername("user124")
                        .withText("text-2")
                        .withTime(Instant.now().toString())
                        .build(),
                CommentDtoTestBuilder.aCommentDto()
                        .withId(1L)
                        .withUsername("user125")
                        .withText("text-3")
                        .withTime(Instant.now().toString())
                        .build()
        );
    }

    private static Stream<Comment> provideComments() {
        return Stream.of(
                CommentTestBuilder.aComment()
                        .withId(1L)
                        .withUsername("user123")
                        .withText("text-1")
                        .withTime(Instant.now())
                        .build(),
                CommentTestBuilder.aComment()
                        .withId(2L)
                        .withUsername("user124")
                        .withText("text-2")
                        .withTime(Instant.now())
                        .build(),
                CommentTestBuilder.aComment()
                        .withId(1L)
                        .withUsername("user125")
                        .withText("text-3")
                        .withTime(Instant.now())
                        .build()
        );
    }
}