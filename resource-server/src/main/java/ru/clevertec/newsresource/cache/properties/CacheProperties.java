package ru.clevertec.newsresource.cache.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@Getter
@Setter
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {
    private String evictStrategy;
    private Integer capacity;
}
