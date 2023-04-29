package ru.clevertec.newsresource.web.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.web.dto.CommentDto;

import java.util.Collection;
import java.util.List;

@Mapper
public interface CommentMapper {
    CommentDto toDto(Comment comment);

    Comment toEntity(CommentDto commentDto);

    List<CommentDto> toDto(Collection<Comment> comments);
}
