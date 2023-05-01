package ru.clevertec.newsresource.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsresource.builder.TestBuilder;
import ru.clevertec.newsresource.web.criteria.NewsCriteria;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aNewsCriteria")
public class NewsCriteriaTestBuilder implements TestBuilder<NewsCriteria> {
    private String title = "";
    private String text = "";

    @Override
    public NewsCriteria build() {
        NewsCriteria newsCriteria = new NewsCriteria();

        newsCriteria.setTitle(title);
        newsCriteria.setText(text);

        return newsCriteria;
    }
}
