package ru.clevertec.newsresource.specifications;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.newsresource.entity.Comment;

@UtilityClass
public class CommentSpecifications {

    public Specification<Comment> hasNewsId(Long newsId) {
        return ((root, query, criteriaBuilder) -> {
            if (newsId == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("news"), newsId);
        });
    }

    public Specification<Comment> hasTextLike(String text) {
        return ((root, query, criteriaBuilder) -> {
            if (text == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("text")),
                    "%" + text.toLowerCase() + "%");
        });
    }

    public Specification<Comment> hasUsernameLike(String username) {
        return ((root, query, criteriaBuilder) -> {
            if (username == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
                    "%" + username.toLowerCase() + "%");
        });
    }

}
