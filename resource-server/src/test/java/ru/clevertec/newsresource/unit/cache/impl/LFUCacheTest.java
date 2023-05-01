package ru.clevertec.newsresource.unit.cache.impl;

import org.junit.jupiter.api.Test;
import ru.clevertec.newsresource.cache.Cache;
import ru.clevertec.newsresource.cache.impl.LFUCache;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LFUCacheTest {

    private static final String CACHE_NAME = "name";

    @Test
    void putShouldUpdateWhenKeyIsPresent() {
        String expectedValue = "new-value-2";
        Cache<String, Object> cache = new LFUCache(2);

        cache.put("key-1", CACHE_NAME, "value-1");
        cache.put("key-2", CACHE_NAME, "value-2");
        cache.put("key-2", CACHE_NAME, expectedValue);
        Optional<Object> actualValue = cache.get("key-2", CACHE_NAME);

        assertThat(actualValue).isPresent().contains(expectedValue);
    }

    @Test
    void putShouldEvictValue() {
        Cache<String, Object> cache = new LFUCache(2);

        cache.put("key-1", CACHE_NAME, "value-1");
        cache.put("key-2", CACHE_NAME, "value-2");
        cache.get("key-1", CACHE_NAME);
        cache.get("key-2", CACHE_NAME);
        cache.put("key-3", CACHE_NAME, "value-3");
        Optional<Object> value = cache.get("key-1", CACHE_NAME);

        assertThat(value).isEmpty();
    }

}