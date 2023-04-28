package ru.clevertec.newsresource.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.clevertec.newsresource.cache.Cache;
import ru.clevertec.newsresource.cache.factory.CacheFactory;
import ru.clevertec.newsresource.cache.properties.CacheProperties;

@Configuration
@Profile({"dev", "test"})
@RequiredArgsConstructor
public class InMemoryCacheConfig {
    private final CacheProperties cacheProperties;

    @Bean
    public Cache<String, Object> cache() {
        return CacheFactory.getInstance().getCache(
                cacheProperties.getEvictStrategy(),
                cacheProperties.getCapacity()
        );
    }

}
