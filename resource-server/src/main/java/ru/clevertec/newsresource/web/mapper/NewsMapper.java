package ru.clevertec.newsresource.web.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.web.dto.NewsDto;

import java.util.Collection;
import java.util.List;

@Mapper
public interface NewsMapper {
    NewsDto toDto(News news);

    News toEntity(NewsDto newsDto);

    List<NewsDto> toDto(Collection<News> news);
}
