package ru.clevertec.newsresource.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.newsresource.criteria.NewsCriteria;
import ru.clevertec.newsresource.entity.News;

import java.util.List;

public interface NewsService {
    List<News> findAllByPageableAndCriteria(Pageable pageable, NewsCriteria criteria);

    News findNewsById(Long newsId);

    News saveNews(News news);

    void updateNewsPartiallyById(Long newsId, News updateNews);

    void deleteNewsById(Long newsId);
}
