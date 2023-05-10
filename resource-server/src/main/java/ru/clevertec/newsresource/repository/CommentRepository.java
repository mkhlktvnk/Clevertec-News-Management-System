package ru.clevertec.newsresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.clevertec.newsresource.entity.Comment;

/**
 * The CommentRepository interface defines database operations for the Comment entity.
 * It extends the JpaRepository interface, providing basic CRUD operations and paging/sorting support.
 * It also extends the JpaSpecificationExecutor interface, providing advanced query capabilities.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
}

