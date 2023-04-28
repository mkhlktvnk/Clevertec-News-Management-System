package ru.clevertec.newsresource.unit.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.newsresource.builder.impl.NewsTestBuilder;
import ru.clevertec.newsresource.criteria.NewsCriteria;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.repository.NewsRepository;
import ru.clevertec.newsresource.service.exception.ResourceNotFoundException;
import ru.clevertec.newsresource.service.impl.NewsServiceImpl;
import ru.clevertec.newsresource.service.message.MessagesSource;
import ru.clevertec.newsresource.service.message.NewsMessageKey;

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

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private MessagesSource messagesSource;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Test
    void findAllByPageableAndCriteriaShouldReturnExpectedNewsAndCallRepository() {
        Pageable pageable = PageRequest.of(0, 3);
        NewsCriteria criteria = NewsCriteria.builder().build();
        List<News> expectedNews = List.of(
                NewsTestBuilder.aNews().build(),
                NewsTestBuilder.aNews().build(),
                NewsTestBuilder.aNews().build()
        );
        doReturn(new PageImpl<>(expectedNews)).when(newsRepository)
                .findAll(any(Specification.class), any(Pageable.class));

        List<News> actualNews = newsService.findAllByPageableAndCriteria(pageable, criteria);

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
        doReturn(expectedNews).when(newsRepository).save(expectedNews);

        News actualNews = newsService.saveNews(expectedNews);

        verify(newsRepository).save(expectedNews);
        assertThat(actualNews).isEqualTo(expectedNews);
    }

    @Test
    void updateNewsPartiallyByIdShouldCallRepository() {
        News updateNews = NewsTestBuilder.aNews().build();
        News newsFromDb = NewsTestBuilder.aNews().build();
        doReturn(Optional.of(newsFromDb)).when(newsRepository).findById(ID);

        newsService.updateNewsPartiallyById(ID, updateNews);

        verify(newsRepository).findById(ID);
        verify(newsRepository).save(newsFromDb);
    }

    @Test
    void updateNewsPartiallyByIdShouldThrowResourceNotFoundException() {
        News updateNews = NewsTestBuilder.aNews().build();
        doReturn(Optional.empty()).when(newsRepository).findById(ID);

        assertThatThrownBy(() -> newsService.updateNewsPartiallyById(ID, updateNews))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, ID));
    }

    @Test
    void deleteNewsByIdShouldCallRepository() {
        News newsFromDb = NewsTestBuilder.aNews().build();
        doReturn(Optional.of(newsFromDb)).when(newsRepository).findById(ID);

        newsService.deleteNewsById(ID);

        verify(newsRepository).findById(ID);
        verify(newsRepository).delete(newsFromDb);
    }

    @Test
    void deleteNewsByIdShouldThrowResourceNotFoundException() {
        doReturn(Optional.empty()).when(newsRepository).findById(ID);

        assertThatThrownBy(() -> newsService.deleteNewsById(ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, ID));
    }
}