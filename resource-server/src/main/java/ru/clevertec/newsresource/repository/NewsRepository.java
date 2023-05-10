package ru.clevertec.newsresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.clevertec.newsresource.entity.News;

/**
 * Repository interface for managing {@link News} entities in the database.
 * Provides CRUD operations and pagination support.
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
}

