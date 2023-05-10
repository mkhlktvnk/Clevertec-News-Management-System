package ru.clevertec.newsresource.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import ru.clevertec.newsresource.entity.News;

import java.util.List;

/**
 * This interface defines methods for managing news.
 */
public interface NewsService {

    /**
     * Retrieves a page of news items that match the given query string, ordered by relevance.
     *
     * @param pageable the page information including the page number and size
     * @param query the query string to search for in the news items
     * @return a list of news items matching the query, ordered by relevance
     */
    List<News> findAllByPageableAndQueryMatch(Pageable pageable, String query);

    /**
     * Retrieves a news item by its unique identifier.
     *
     * @param newsId the ID of the news item to retrieve
     * @return the news item with the given ID, or null if no such news item exists
     */
    News findNewsById(Long newsId);

    /**
     * Saves a new news item or updates an existing one.
     *
     * @param news the news item to save or update
     * @param user the user who is saving or updating the news item
     * @return the saved or updated news item
     */
    News saveNews(News news, User user);

    /**
     * Updates some fields of a news item identified by its unique identifier.
     *
     * @param newsId the ID of the news item to update
     * @param updateNews the news item with the fields to update
     * @param user the user who is updating the news item
     */
    void updateNewsPartiallyById(Long newsId, News updateNews, User user);

    /**
     * Deletes a news item by its unique identifier.
     *
     * @param newsId the ID of the news item to delete
     * @param user the user who is deleting the news item
     */
    void deleteNewsById(Long newsId, User user);
}

