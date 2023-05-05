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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import ru.clevertec.exception.handling.starter.exception.ResourceNotFoundException;
import ru.clevertec.newsresource.builder.impl.NewsTestBuilder;
import ru.clevertec.newsresource.builder.impl.UserTestBuilder;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.repository.NewsRepository;
import ru.clevertec.newsresource.service.impl.NewsPermissionService;
import ru.clevertec.newsresource.service.impl.NewsServiceImpl;
import ru.clevertec.newsresource.service.message.MessagesSource;
import ru.clevertec.newsresource.service.message.key.NewsMessageKey;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    private static final Long ID = 1L;

    private static final String QUERY = "Interesting thing happened this night";

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private MessagesSource messagesSource;

    @Mock
    private NewsPermissionService newsPermissionService;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Test
    void findAllByPageableAndQueryMatchShouldReturnExpectedNewsAndCallRepository() {
        Pageable pageable = PageRequest.of(0, 3);
        List<News> expectedNews = List.of(
                NewsTestBuilder.aNews().build(),
                NewsTestBuilder.aNews().build(),
                NewsTestBuilder.aNews().build()
        );
        doReturn(new PageImpl<>(expectedNews)).when(newsRepository)
                .findAll(any(Specification.class), any(Pageable.class));

        List<News> actualNews = newsService.findAllByPageableAndQueryMatch(pageable, QUERY);

        verify(newsRepository).findAll(any(Specification.class), any(Pageable.class));
        assertThat(actualNews).isEqualTo(expectedNews);
    }

    @Test
    void findNewsByIdShouldReturnExpectedNewsAndCallRepository() {
        News expectedNews = NewsTestBuilder.aNews().build();
        doReturn(Optional.of(expectedNews)).when(newsRepository).findById(ID);

        News actualNews = newsService.findNewsById(ID);

        verify(newsRepository).findById(ID);
        assertThat(actualNews).isEqualTo(expectedNews);
    }

    @Test
    void findNewsByIdShouldThrowResourceNotFoundException() {
        doReturn(Optional.empty()).when(newsRepository).findById(ID);

        assertThatThrownBy(() -> newsService.findNewsById(ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, ID));
    }

    @Test
    void saveNewsShouldReturnSavedNewsAndCallRepository() {
        News expectedNews = NewsTestBuilder.aNews().build();
        User user = UserTestBuilder.anUser().withUsername("user-123").build();
        doReturn(expectedNews).when(newsRepository).save(expectedNews);

        News actualNews = newsService.saveNews(expectedNews, user);

        verify(newsRepository).save(expectedNews);
        assertThat(actualNews).isEqualTo(expectedNews);
    }

    @Test
    void updateNewsPartiallyByIdShouldCallRepository() {
        News updateNews = NewsTestBuilder.aNews().build();
        News newsFromDb = NewsTestBuilder.aNews().build();
        User user = UserTestBuilder.anUser().withUsername("user-123").build();
        doReturn(Optional.of(newsFromDb)).when(newsRepository).findById(ID);
        doReturn(true).when(newsPermissionService).userHasPermissionToEditResource(user, newsFromDb);

        newsService.updateNewsPartiallyById(ID, updateNews, user);

        verify(newsRepository).findById(ID);
        verify(newsRepository).save(newsFromDb);
    }

    @Test
    void updateNewsPartiallyByIdShouldThrowResourceNotFoundException() {
        News updateNews = NewsTestBuilder.aNews().build();
        User user = UserTestBuilder.anUser().withUsername("user-123").build();
        doReturn(Optional.empty()).when(newsRepository).findById(ID);

        assertThatThrownBy(() -> newsService.updateNewsPartiallyById(ID, updateNews, user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, ID));
    }

    @Test
    void updateNewsPartiallyByIdShouldThrowAccessDeniedException() {
        News updateNews = NewsTestBuilder.aNews().build();
        News newsFromDb = NewsTestBuilder.aNews().build();
        User user = UserTestBuilder.anUser().withUsername("user-123").build();
        doReturn(Optional.of(newsFromDb)).when(newsRepository).findById(ID);
        doReturn(false).when(newsPermissionService).userHasPermissionToEditResource(user, newsFromDb);

        assertThatThrownBy(() -> newsService.updateNewsPartiallyById(ID, updateNews, user))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage(messagesSource.get(NewsMessageKey.UNABLE_TO_EDIT));
    }

    @Test
    void deleteNewsByIdShouldCallRepository() {
        News newsFromDb = NewsTestBuilder.aNews().build();
        User user = UserTestBuilder.anUser().withUsername("user-123").build();
        doReturn(Optional.of(newsFromDb)).when(newsRepository).findById(ID);
        doReturn(true).when(newsPermissionService).userHasPermissionToEditResource(user, newsFromDb);

        newsService.deleteNewsById(ID, user);

        verify(newsRepository).findById(ID);
        verify(newsRepository).delete(newsFromDb);
    }

    @Test
    void deleteNewsByIdShouldThrowResourceNotFoundException() {
        User user = UserTestBuilder.anUser().withUsername("user-123").build();
        doReturn(Optional.empty()).when(newsRepository).findById(ID);

        assertThatThrownBy(() -> newsService.deleteNewsById(ID, user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, ID));
    }

    @Test
    void deleteNewsByIdShouldThrowAccessDeniedException() {
        News newsFromDb = NewsTestBuilder.aNews().build();
        User user = UserTestBuilder.anUser().withUsername("user-123").build();
        doReturn(Optional.of(newsFromDb)).when(newsRepository).findById(ID);
        doReturn(false).when(newsPermissionService).userHasPermissionToEditResource(user, newsFromDb);

        assertThatThrownBy(() -> newsService.deleteNewsById(ID, user))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage(messagesSource.get(NewsMessageKey.UNABLE_TO_DELETE));
    }
}