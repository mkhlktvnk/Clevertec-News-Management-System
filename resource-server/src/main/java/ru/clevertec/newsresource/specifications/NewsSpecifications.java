package ru.clevertec.newsresource.specifications;

import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.newsresource.entity.News;


@UtilityClass
public class NewsSpecifications {

    public Specification<News> hasMatchWithQuery(String searchQuery) {
        return ((root, query, criteriaBuilder) -> {
           if (searchQuery == null) {
               return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
           }

           String pattern = "%" + searchQuery.toLowerCase() + "%";
           Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern);
           Predicate textPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("text")), pattern);

           return criteriaBuilder.or(titlePredicate, textPredicate);
        });
    }

}
