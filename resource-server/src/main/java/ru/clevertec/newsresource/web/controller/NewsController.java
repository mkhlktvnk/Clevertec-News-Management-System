package ru.clevertec.newsresource.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsresource.criteria.NewsCriteria;
import ru.clevertec.newsresource.entity.News;
import ru.clevertec.newsresource.service.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api/v0/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<List<News>> findAllByPageableAndCriteria(
            @PageableDefault Pageable pageable, @Valid NewsCriteria criteria) {
        List<News> news = newsService.findAllByPageableAndCriteria(pageable, criteria);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> findNewsById(@PathVariable Long id) {
        News news = newsService.findNewsById(id);
        return ResponseEntity.ok(news);
    }

    @PostMapping
    public ResponseEntity<News> saveNews(@RequestBody News news) {
        News savedNews = newsService.saveNews(news);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNews);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNewsPartiallyById(@PathVariable Long id, @RequestBody News updateNews) {
        newsService.updateNewsPartiallyById(id, updateNews);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNewsById(@PathVariable Long id) {
        newsService.deleteNewsById(id);
    }
}
