package ru.clevertec.newsresource.cache.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the cache.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    /**
     * The eviction strategy used by the cache.
     */
    private String evictStrategy;

    /**
     * The maximum capacity of the cache.
     */
    private Integer capacity;
}
