package ru.clevertec.newsresource.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsresource.builder.TestBuilder;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.entity.News;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aNews")
public class NewsTestBuilder implements TestBuilder<News> {
    private Long id = 0L;
    private Instant time = Instant.now();
    private String title = "";
    private String text = "";
    private List<Comment> comments = new ArrayList<>();

    @Override
    public News build() {
        News news = new News();

        news.setId(id);
        news.setTime(time);
        news.setTitle(title);
        news.setText(text);
        news.setComments(comments);

        return news;
    }
}
