package ru.clevertec.newsresource.builder.impl;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import org.hibernate.annotations.CreationTimestamp;
import ru.clevertec.newsresource.builder.TestBuilder;
import ru.clevertec.newsresource.entity.Comment;
import ru.clevertec.newsresource.entity.News;

import java.time.Instant;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aComment")
public class CommentTestBuilder implements TestBuilder<Comment> {
    private Long id;
    private Instant time;
    private String username;
    private String text;
    private News news;

    @Override
    public Comment build() {
        Comment comment = new Comment();

        comment.setId(id);
        comment.setTime(time);
        comment.setUsername(username);
        comment.setText(text);
        comment.setNews(news);

        return comment;
    }
}
