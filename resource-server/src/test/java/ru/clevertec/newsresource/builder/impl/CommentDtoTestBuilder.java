package ru.clevertec.newsresource.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsresource.builder.TestBuilder;
import ru.clevertec.newsresource.web.dto.CommentDto;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentDto")
public class CommentDtoTestBuilder implements TestBuilder<CommentDto> {

    private Long id = 0L;
    private String time = "";
    private String username = "";
    private String text = "";
    private Long newsId = 0L;

    @Override
    public CommentDto build() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(id);
        commentDto.setTime(time);
        commentDto.setUsername(username);
        commentDto.setText(text);
        commentDto.setNewsId(newsId);
        return commentDto;
    }
}
