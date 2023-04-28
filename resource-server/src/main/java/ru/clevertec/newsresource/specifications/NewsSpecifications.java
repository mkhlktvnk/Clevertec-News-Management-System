package ru.clevertec.newsresource.specifications;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.newsresource.entity.News;

@UtilityClass
public class NewsSpecifications {

    public Specification<News> hasTitleLike(String title) {
        return ((root, query, criteriaBuilder) -> {
            if (title == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("title"), "%" + title + "%");
        });
    }

    public Specification<News> hasTextLike(String text) {
        return ((root, query, criteriaBuilder) -> {
            if (text == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("text"), "%" + text + "%");
        });
    }

}
