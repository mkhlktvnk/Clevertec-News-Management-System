package ru.clevertec.newsresource.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsresource.builder.TestBuilder;
import ru.clevertec.newsresource.web.dto.NewsDto;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aNewsDto")
public class NewsDtoTestBuilder implements TestBuilder<NewsDto> {

    private Long id = 0L;
    private String username = "";
    private String time = "";
    private String title = "";
    private String text = "";

    @Override
    public NewsDto build() {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(id);
        newsDto.setUsername(username);
        newsDto.setTime(time);
        newsDto.setTitle(title);
        newsDto.setText(text);
        return newsDto;
    }
}
