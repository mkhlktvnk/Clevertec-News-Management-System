package ru.clevertec.newsresource.web.criteria;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCriteria {

    @Size(min = 1)
    private String text;

}
