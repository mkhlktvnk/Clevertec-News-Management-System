package ru.clevertec.newsresource.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a news article that can be created, read, updated, and deleted.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "news")
public class News implements Identifiable<Long> {

    /**
     * The unique identifier for the news article.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username of the author who created the news article.
     */
    @Column(nullable = false, updatable = false)
    private String username;

    /**
     * The timestamp of when the news article was created.
     */
    @CreationTimestamp
    @Column(nullable = false)
    private Instant time;

    /**
     * The title of the news article.
     */
    @Column(nullable = false)
    private String title;

    /**
     * The text content of the news article.
     */
    @Column(nullable = false)
    private String text;

    /**
     * The list of comments associated with the news article.
     */
    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

}

