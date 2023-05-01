package ru.clevertec.newsresource.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsresource.builder.TestBuilder;
import ru.clevertec.newsresource.web.criteria.CommentCriteria;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aComment")
public class CommentCriteriaTestBuilder implements TestBuilder<CommentCriteria> {
    private String text = "";
    private String username = "";

    @Override
    public CommentCriteria build() {
        CommentCriteria commentCriteria = new CommentCriteria();

        commentCriteria.setText(text);
        commentCriteria.setUsername(username);

        return commentCriteria;
    }
}
