package ru.clevertec.newsresource.specifications;

import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.newsresource.entity.Comment;

@UtilityClass
public class CommentSpecifications {

    public Specification<Comment> hasMatchWithQuery(String searchQuery) {
        return ((root, query, criteriaBuilder) -> {
            if (searchQuery == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            String pattern = "%" + searchQuery.toLowerCase() + "%";
            Predicate textPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("text")), pattern);
            Predicate usernamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), pattern);

            return criteriaBuilder.or(textPredicate, usernamePredicate);
        });
    }

    public Specification<Comment> hasNewsId(Long newsId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("news"), newsId));
    }

}
