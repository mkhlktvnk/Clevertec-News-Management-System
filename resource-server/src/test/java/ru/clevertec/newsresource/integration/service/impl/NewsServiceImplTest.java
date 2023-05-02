package ru.clevertec.newsresource.integration.service.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.exception.handling.starter.exception.ResourceNotFoundException;
import ru.clevertec.newsresource.builder.impl.NewsTestBuilder;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.integration.BaseIntegrationTest;
import ru.clevertec.newsresource.service.NewsService;
import ru.clevertec.newsresource.service.message.MessagesSource;
import ru.clevertec.newsresource.service.message.key.NewsMessageKey;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NewsServiceImplTest extends BaseIntegrationTest {
    public static final Long CORRECT_NEWS_ID = 1L;

    public static final Long INCORRECT_NEWS_ID = 30L;

    public static final String CORRECT_QUERY = "SpaceX";

    public static final String INCORRECT_QUERY = "Query that will not find anything";

    @Autowired
    private NewsService newsService;

    @Autowired
    private MessagesSource messagesSource;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findAllByPageableAndQueryMatchShouldReturnExpectedCountOfNews() {
        Pageable pageable = PageRequest.of(0, 1);

        List<News> news = newsService.findAllByPageableAndQueryMatch(pageable, CORRECT_QUERY);

        assertThat(news.size()).isEqualTo(1);
    }

    @Test
    void findAllByPageableAndQueryMatchShouldReturnEmptyList() {
        Pageable pageable = PageRequest.of(0, 1);

        List<News> news = newsService.findAllByPageableAndQueryMatch(pageable, INCORRECT_QUERY);

        assertThat(news.size()).isEqualTo(0);
    }

    @Test
    void findNewsByIdShouldFindNews() {
        News news = newsService.findNewsById(CORRECT_NEWS_ID);

        assertThat(news).isNotNull();
        assertThat(news.getId()).isEqualTo(CORRECT_NEWS_ID);
    }

    @Test
    void findNewsByIdShouldThrowResourceNotFoundException() {
        assertThatThrownBy(() -> newsService.findNewsById(INCORRECT_NEWS_ID))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void saveNewsShouldReturnNewsWithNotNullId() {
        News news = NewsTestBuilder.aNews()
                .withTitle("New-title")
                .withText("New-text")
                .build();

        News actual = newsService.saveNews(news);

        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void updateNewsPartiallyByIdShouldUpdateNews() {
        News updateNews = NewsTestBuilder.aNews()
                .withTitle("new-title").build();

        newsService.updateNewsPartiallyById(CORRECT_NEWS_ID, updateNews);
        entityManager.flush();
    }

    @Test
    void updateNewsPartiallyByIdShouldThrowResourceNotFoundException() {
        News updateNews = NewsTestBuilder.aNews()
                .withTitle("new-title").build();

        assertThatThrownBy(() -> newsService.updateNewsPartiallyById(INCORRECT_NEWS_ID, updateNews))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, INCORRECT_NEWS_ID));
    }

    @Test
    void deleteNewsByIdShouldDeleteNews() {
        newsService.deleteNewsById(CORRECT_NEWS_ID);
        entityManager.flush();
    }

    @Test
    void deleteNewsByIdShouldThrowResourceNotFoundException() {
        assertThatThrownBy(() -> newsService.deleteNewsById(INCORRECT_NEWS_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, INCORRECT_NEWS_ID));
    }
}