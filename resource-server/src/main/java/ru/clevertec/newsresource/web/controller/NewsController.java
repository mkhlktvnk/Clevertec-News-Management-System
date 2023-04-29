package ru.clevertec.newsresource.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
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
import ru.clevertec.newsresource.web.dto.NewsDto;
import ru.clevertec.newsresource.web.mapper.NewsMapper;

import java.util.List;

@RestController
@RequestMapping("/api/v0/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;
    private final NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);

    @GetMapping
    public ResponseEntity<List<NewsDto>> findAllByPageableAndCriteria(
            @PageableDefault Pageable pageable, @Valid NewsCriteria criteria) {
        List<News> news = newsService.findAllByPageableAndCriteria(pageable, criteria);
        return ResponseEntity.ok(newsMapper.toDto(news));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> findNewsById(@PathVariable Long id) {
        News news = newsService.findNewsById(id);
        return ResponseEntity.ok(newsMapper.toDto(news));
    }

    @PostMapping
    public ResponseEntity<NewsDto> saveNews(@RequestBody NewsDto news) {
        News savedNews = newsService.saveNews(newsMapper.toEntity(news));
        return ResponseEntity.status(HttpStatus.CREATED).body(newsMapper.toDto(savedNews));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNewsPartiallyById(@PathVariable Long id, @RequestBody NewsDto updateNews) {
        newsService.updateNewsPartiallyById(id, newsMapper.toEntity(updateNews));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNewsById(@PathVariable Long id) {
        newsService.deleteNewsById(id);
    }
}
