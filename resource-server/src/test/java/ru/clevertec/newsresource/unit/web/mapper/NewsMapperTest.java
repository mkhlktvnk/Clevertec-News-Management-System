package ru.clevertec.newsresource.unit.web.mapper;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import ru.clevertec.newsresource.builder.impl.NewsDtoTestBuilder;
import ru.clevertec.newsresource.builder.impl.NewsTestBuilder;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.web.dto.NewsDto;
import ru.clevertec.newsresource.web.mapper.NewsMapper;

import java.time.Instant;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class NewsMapperTest {

    private final NewsMapper mapper = Mappers.getMapper(NewsMapper.class);

    @ParameterizedTest
    @MethodSource("provideNews")
    void toDtoShouldMapToNewsDto(News news) {
        NewsDto newsDto = mapper.toDto(news);

        assertThat(newsDto.getId()).isEqualTo(news.getId());
        assertThat(newsDto.getTitle()).isEqualTo(news.getTitle());
        assertThat(newsDto.getText()).isEqualTo(news.getText());
        assertThat(newsDto.getUsername()).isEqualTo(news.getUsername());
        assertThat(newsDto.getTime()).isEqualTo(news.getTime().toString());
    }

    @ParameterizedTest
    @MethodSource("provideNewsDto")
    void toEntityShouldMapToNews(NewsDto newsDto) {
        News news = mapper.toEntity(newsDto);

        assertThat(news.getId()).isEqualTo(newsDto.getId());
        assertThat(news.getTitle()).isEqualTo(newsDto.getTitle());
        assertThat(news.getText()).isEqualTo(newsDto.getText());
        assertThat(news.getUsername()).isEqualTo(newsDto.getUsername());
        assertThat(news.getTime()).isEqualTo(newsDto.getTime().toString());
    }

    private static Stream<News> provideNews() {
        return Stream.of(
                NewsTestBuilder.aNews()
                        .withId(1L)
                        .withUsername("user123")
                        .withTitle("title-1")
                        .withText("text-1")
                        .withTime(Instant.now())
                        .build(),
                NewsTestBuilder.aNews()
                        .withId(2L)
                        .withUsername("user124")
                        .withTitle("title-2")
                        .withText("text-2")
                        .withTime(Instant.now())
                        .build(),
                NewsTestBuilder.aNews()
                        .withId(2L)
                        .withUsername("user124")
                        .withTitle("title-3")
                        .withText("text-3")
                        .withTime(Instant.now())
                        .build()
        );
    }

    private static Stream<NewsDto> provideNewsDto() {
        return Stream.of(
                NewsDtoTestBuilder.aNewsDto()
                        .withId(1L)
                        .withUsername("user123")
                        .withTitle("title-1")
                        .withText("text-1")
                        .withTime(Instant.now().toString())
                        .build(),
                NewsDtoTestBuilder.aNewsDto()
                        .withId(2L)
                        .withUsername("user124")
                        .withTitle("title-2")
                        .withText("text-2")
                        .withTime(Instant.now().toString())
                        .build(),
                NewsDtoTestBuilder.aNewsDto()
                        .withId(2L)
                        .withUsername("user124")
                        .withTitle("title-3")
                        .withText("text-3")
                        .withTime(Instant.now().toString())
                        .build()
        );
    }
}