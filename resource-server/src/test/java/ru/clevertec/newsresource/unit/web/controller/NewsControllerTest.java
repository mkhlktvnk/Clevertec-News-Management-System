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
import ru.clevertec.newsresource.builder.impl.NewsDtoTestBuilder;
import ru.clevertec.newsresource.builder.impl.NewsTestBuilder;
import ru.clevertec.newsresource.builder.impl.UserTestBuilder;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.service.NewsService;
import ru.clevertec.newsresource.web.controller.NewsController;
import ru.clevertec.newsresource.web.dto.NewsDto;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NewsControllerTest {

    private static final Long NEWS_ID = 1L;

    @Mock
    private NewsService newsService;

    @InjectMocks
    private NewsController newsController;

    @Test
    void findAllByPageableAndMatchWithQueryShouldReturnCorrectCountOfNewsAndOkStatus() {
        String query = "query";
        Pageable pageable = PageRequest.of(0, 3);
        List<News> news = List.of(
                NewsTestBuilder.aNews().build(),
                NewsTestBuilder.aNews().build(),
                NewsTestBuilder.aNews().build()
        );
        doReturn(news).when(newsService).findAllByPageableAndQueryMatch(pageable, query);

        ResponseEntity<List<NewsDto>> response = newsController.findAllByPageableAndMatchWithQuery(pageable, query);

        verify(newsService).findAllByPageableAndQueryMatch(pageable, query);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(news.size());
    }

    @Test
    void findNewsByIdShouldReturnCorrectNewsAndOkStatus() {
        News news = NewsTestBuilder.aNews().withId(NEWS_ID).build();
        doReturn(news).when(newsService).findNewsById(NEWS_ID);

        ResponseEntity<NewsDto> response = newsController.findNewsById(NEWS_ID);

        verify(newsService).findNewsById(NEWS_ID);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(news.getId());
    }

    @Test
    void saveNewsShouldReturnSavedNewsAndCreatedStatus() {
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withRoles(List.of(new SimpleGrantedAuthority("JOURNALIST")))
                .build();
        News news = NewsTestBuilder.aNews()
                .withTitle("New news")
                .withTime(Instant.now())
                .build();
        NewsDto newsDto = NewsDtoTestBuilder.aNewsDto()
                .withTitle("New news")
                .withTime(Instant.now().toString())
                .build();
        doReturn(news).when(newsService).saveNews(any(News.class), any(User.class));

        ResponseEntity<NewsDto> response = newsController.saveNews(newsDto, user);

        verify(newsService).saveNews(any(News.class), any(User.class));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getTitle()).isEqualTo(news.getTitle());
    }

    @Test
    void updateNewsPartiallyByIdShouldCallService() {
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withRoles(List.of(new SimpleGrantedAuthority("JOURNALIST")))
                .build();
        NewsDto newsDto = NewsDtoTestBuilder.aNewsDto()
                .withTitle("New news title")
                .withTime(Instant.now().toString())
                .build();

        newsController.updateNewsPartiallyById(NEWS_ID, newsDto, user);

        verify(newsService).updateNewsPartiallyById(any(Long.class), any(News.class), any(User.class));
    }

    @Test
    void deleteNewsByIdShouldCallService() {
        User user = UserTestBuilder.anUser()
                .withUsername("user123")
                .withRoles(List.of(new SimpleGrantedAuthority("JOURNALIST")))
                .build();

        newsController.deleteNewsById(NEWS_ID, user);

        verify(newsService).deleteNewsById(NEWS_ID, user);
    }
}