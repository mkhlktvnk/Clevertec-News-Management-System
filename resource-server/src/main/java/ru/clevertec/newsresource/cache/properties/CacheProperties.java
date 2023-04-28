package ru.clevertec.newsresource.cache.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {
    private String evictStrategy;
    private Integer capacity;
}
