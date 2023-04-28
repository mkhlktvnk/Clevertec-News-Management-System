package ru.clevertec.newsresource.specifications;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.newsresource.entity.Comment;

@UtilityClass
public class CommentSpecifications {

    public Specification<Comment> hasTextLike(String text) {
        return ((root, query, criteriaBuilder) -> {
            if (text == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("text"), "%" + text + "%");
        });
    }

}