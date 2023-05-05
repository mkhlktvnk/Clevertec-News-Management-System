package ru.clevertec.newsresource.web.mapper;

import org.mapstruct.*;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.web.dto.CommentDto;

import java.util.Collection;
import java.util.List;

@Mapper
public interface CommentMapper {
    CommentDto toDto(Comment comment);

    Comment toEntity(CommentDto commentDto);

    List<CommentDto> toDto(Collection<Comment> comments);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "time", ignore = true),
            @Mapping(target = "news", ignore = true),
            @Mapping(target = "username", ignore = true),
            @Mapping(
                    target = "text",
                    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
            ),
    })
    void mapNotNullFields(@MappingTarget Comment target, Comment source);
}
