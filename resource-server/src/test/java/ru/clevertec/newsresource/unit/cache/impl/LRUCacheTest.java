package ru.clevertec.newsresource.unit.cache.impl;

import org.junit.jupiter.api.Test;
import ru.clevertec.newsresource.cache.Cache;
import ru.clevertec.newsresource.cache.impl.LRUCache;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LRUCacheTest {

    private static final String CACHE_PREFIX = "name";

    @Test
    void putShouldEvictValue() {
        Cache<String, Object> cache = new LRUCache(2);
        cache.put("key1", CACHE_PREFIX, "value1");
        cache.put("key2", CACHE_PREFIX, "value2");
        cache.put("key3", CACHE_PREFIX, "value3");

        Optional<Object> result = cache.get("key1", CACHE_PREFIX);

        assertThat(result).isEmpty();
    }

    @Test
    void putShouldUpdateWhenEntryIsAlreadyPresent() {
        String expectedValue = "new-value2";
        Cache<String, Object> cache = new LRUCache(2);

        cache.put("key1", CACHE_PREFIX, "value1");
        cache.put("key2", CACHE_PREFIX, "value2");
        cache.put("key2", CACHE_PREFIX, expectedValue);
        Optional<Object> actualValue = cache.get("key2", CACHE_PREFIX);

        assertThat(actualValue).isPresent().contains(expectedValue);
    }

    @Test
    void evictShouldEvictValue() {
        Cache<String, Object> cache = new LRUCache(2);

        cache.put("key1", CACHE_PREFIX, "value1");
        cache.put("key2", CACHE_PREFIX, "value2");
        cache.evict("key1", CACHE_PREFIX);
        Optional<Object> value = cache.get("key1", CACHE_PREFIX);

        assertThat(value).isEmpty();
    }
}