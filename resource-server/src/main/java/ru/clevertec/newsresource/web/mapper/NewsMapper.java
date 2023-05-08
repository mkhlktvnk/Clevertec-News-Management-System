package ru.clevertec.newsresource.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.web.dto.NewsDto;

import java.util.Collection;
import java.util.List;

@Mapper
public interface NewsMapper {
    NewsDto toDto(News news);

    News toEntity(NewsDto newsDto);

    List<NewsDto> toDto(Collection<News> news);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "time", ignore = true),
            @Mapping(target = "comments", ignore = true),
            @Mapping(
                    target = "title",
                    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
            ),
            @Mapping(
                    target = "text",
                    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
            )
    })
    void mapNotNullFields(@MappingTarget News target, News source);
}
