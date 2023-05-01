package ru.clevertec.newsresource.builder.impl;

import ru.clevertec.newsresource.builder.TestBuilder;
import ru.clevertec.newsresource.web.criteria.CommentCriteria;

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
