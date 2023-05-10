package ru.clevertec.newsresource.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
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

/**
 * Service implementation for managing news entities.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final MessagesSource messagesSource;
    private final PermissionService<User, News> permissionService;
    private final NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);

    /**
     * Finds a page of news matching the given query, sorted and paged as specified.
     *
     * @param pageable the pagination information
     * @param query    the search query
     * @return a list of news matching the query, sorted and paged as specified
     */
    @Override
    @Loggable
    public List<News> findAllByPageableAndQueryMatch(Pageable pageable, String query) {
        Specification<News> searchSpecification =
                Specification.where(NewsSpecifications.hasMatchWithQuery(query));

        return newsRepository.findAll(searchSpecification, pageable).getContent();
    }

    /**
     * Finds a news entity by its ID.
     *
     * @param newsId the ID of the news to find
     * @return the news entity with the specified ID
     * @throws ResourceNotFoundException if no news entity exists with the specified ID
     */
    @Override
    @Loggable
    @Cacheable(value = "news", key = "#newsId")
    public News findNewsById(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messagesSource.get(NewsMessageKey.NOT_FOUND_BY_ID, newsId)
                ));
    }

    /**
     * Saves a news entity.
     *
     * @param news the news entity to save
     * @param user the user performing the operation
     * @return the saved news entity
     */
    @Override
    @Loggable
    @Transactional
    @CachePut(value = "news", key = "#result.id")
    public News saveNews(News news, User user) {
        news.setUsername(user.getUsername());
        return newsRepository.save(news);
    }

    /**
     * Updates some fields of a news entity by its ID.
     *
     * @param newsId     the ID of the news to update
     * @param updateNews the news entity with the fields to update
     * @param user       the user performing the operation
     * @throws ResourceNotFoundException if no news entity exists with the specified ID
     * @throws AccessDeniedException     if the user does not have permission to edit the news entity
     */
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

    /**
     * Deletes a news entity by its ID.
     *
     * @param newsId the ID of the news to delete
     * @param user   the user performing the operation
     * @throws ResourceNotFoundException if no news entity exists with the specified ID
     * @throws AccessDeniedException     if the user does not have permission to delete the news entity
     **/
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