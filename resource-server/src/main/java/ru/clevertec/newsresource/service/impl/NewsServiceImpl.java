package ru.clevertec.newsresource.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsresource.web.criteria.NewsCriteria;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.repository.NewsRepository;
import ru.clevertec.newsresource.service.NewsService;
import ru.clevertec.newsresource.service.exception.ResourceNotFoundException;
import ru.clevertec.newsresource.service.message.MessagesSource;
import ru.clevertec.newsresource.service.message.key.NewsMessageKey;
import ru.clevertec.newsresource.specifications.NewsSpecifications;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final MessagesSource messagesSource;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<News> findAllByPageableAndCriteria(Pageable pageable, NewsCriteria criteria) {
        Specification<News> searchSpecification = Specification.where(NewsSpecifications.hasTitleLike(criteria.getTitle()))
                .and(NewsSpecifications.hasTextLike(criteria.getTitle()));

        return newsRepository.findAll(searchSpecification, pageable).getContent();
    }

    @Override
    public News findNewsById(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, newsId)
                ));
    }

    @Override
    @Transactional
    public News saveNews(News news) {
        return newsRepository.save(news);
    }

    @Override
    @Transactional
    public void updateNewsPartiallyById(Long newsId, News updateNews) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, newsId)
                ));
        modelMapper.map(updateNews, news);
        newsRepository.save(news);
    }

    @Override
    @Transactional
    public void deleteNewsById(Long newsId) {
        News newsToDelete = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, newsId)
                ));
        newsRepository.delete(newsToDelete);
    }
}