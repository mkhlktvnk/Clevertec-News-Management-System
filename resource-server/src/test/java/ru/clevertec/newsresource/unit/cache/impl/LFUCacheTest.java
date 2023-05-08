package ru.clevertec.newsresource.unit.cache.impl;

import org.junit.jupiter.api.Test;
import ru.clevertec.newsresource.cache.Cache;
import ru.clevertec.newsresource.cache.impl.LFUCache;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LFUCacheTest {

    private static final String CACHE_PREFIX = "name";

    @Test
    void putShouldUpdateWhenKeyIsPresent() {
        String expectedValue = "new-value-2";
        Cache<String, Object> cache = new LFUCache(2);

        cache.put("key-1", CACHE_PREFIX, "value-1");
        cache.put("key-2", CACHE_PREFIX, "value-2");
        cache.put("key-2", CACHE_PREFIX, expectedValue);
        Optional<Object> actualValue = cache.get("key-2", CACHE_PREFIX);

        assertThat(actualValue).isPresent().contains(expectedValue);
    }

    @Test
    void putShouldEvictValue() {
        Cache<String, Object> cache = new LFUCache(2);

        cache.put("key-1", CACHE_PREFIX, "value-1");
        cache.put("key-2", CACHE_PREFIX, "value-2");
        cache.get("key-1", CACHE_PREFIX);
        cache.get("key-2", CACHE_PREFIX);
        cache.put("key-3", CACHE_PREFIX, "value-3");
        Optional<Object> value = cache.get("key-1", CACHE_PREFIX);

        assertThat(value).isEmpty();
    }

    @Test
    void evictShouldRemoveEntry() {
        Cache<String, Object> cache = new LFUCache(2);

        cache.put("key-1", CACHE_PREFIX, "value-1");
        cache.put("key-2", CACHE_PREFIX, "value-2");
        cache.evict("key-1", CACHE_PREFIX);
        Optional<Object> value = cache.get("key-1", CACHE_PREFIX);

        assertThat(value).isEmpty();
    }

}