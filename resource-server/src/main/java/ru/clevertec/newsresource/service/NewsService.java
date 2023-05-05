package ru.clevertec.newsresource.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import ru.clevertec.newsresource.entity.News;

import java.util.List;

public interface NewsService {
    List<News> findAllByPageableAndQueryMatch(Pageable pageable, String query);

    News findNewsById(Long newsId);

    News saveNews(News news, User user);

    void updateNewsPartiallyById(Long newsId, News updateNews, User user);

    void deleteNewsById(Long newsId, User user);
}
