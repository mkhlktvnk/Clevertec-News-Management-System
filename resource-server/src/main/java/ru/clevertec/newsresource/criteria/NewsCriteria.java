package ru.clevertec.newsresource.criteria;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewsCriteria {

    @Size(min = 1)
    private String title;

    @Size(min = 1)
    private String text;

}
