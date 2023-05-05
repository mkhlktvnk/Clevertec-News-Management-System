package ru.clevertec.newsresource.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exception.handling.starter.exception.ResourceNotFoundException;
import ru.clevertec.logging.annotation.Loggable;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.repository.NewsRepository;
import ru.clevertec.newsresource.service.NewsService;
import ru.clevertec.newsresource.service.PermissionService;
import ru.clevertec.newsresource.service.message.MessagesSource;
import ru.clevertec.newsresource.service.message.key.NewsMessageKey;
import ru.clevertec.newsresource.specifications.NewsSpecifications;
import ru.clevertec.newsresource.web.mapper.NewsMapper;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final MessagesSource messagesSource;
    private final PermissionService<User, News> permissionService;
    private final NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);

    @Override
    @Loggable
    public List<News> findAllByPageableAndQueryMatch(Pageable pageable, String query) {
        Specification<News> searchSpecification =
                Specification.where(NewsSpecifications.hasMatchWithQuery(query));

        return newsRepository.findAll(searchSpecification, pageable).getContent();
    }

    @Override
    @Loggable
    @Cacheable(value = "news", key = "#newsId")
    public News findNewsById(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, newsId)
                ));
    }

    @Override
    @Loggable
    @Transactional
    @CachePut(value = "news", key = "#result.id")
    public News saveNews(News news, User user) {
        news.setUsername(user.getUsername());
        return newsRepository.save(news);
    }

    @Override
    @Loggable
    @Transactional
    @CachePut(value = "news", key = "#newsId")
    public void updateNewsPartiallyById(Long newsId, News updateNews, User user) {
        News newsToUpdate = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, newsId)
                ));

        if (!permissionService.userHasPermissionToEditResource(user, newsToUpdate)) {
            throw new AccessDeniedException(messagesSource.get(NewsMessageKey.UNABLE_TO_EDIT));
        }

        newsMapper.mapNotNullFields(newsToUpdate, updateNews);
        newsRepository.save(newsToUpdate);
    }

    @Override
    @Loggable
    @Transactional
    @CacheEvict(value = "news", key = "#newsId")
    public void deleteNewsById(Long newsId, User user) {
        News newsToDelete = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, newsId)
                ));

        if (!permissionService.userHasPermissionToEditResource(user, newsToDelete)) {
            throw new AccessDeniedException(messagesSource.get(NewsMessageKey.UNABLE_TO_DELETE));
        }

        newsRepository.delete(newsToDelete);
    }
}