package ru.clevertec.newsresource.unit.cache.impl;

import org.junit.jupiter.api.Test;
import ru.clevertec.newsresource.cache.Cache;
import ru.clevertec.newsresource.cache.impl.LRUCache;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LRUCacheTest {

    @Test
    void putShouldEvictValue() {
        Cache<String, Object> cache = new LRUCache(2);
        cache.put("key1", "name", "value1");
        cache.put("key2", "name", "value2");
        cache.put("key3", "name", "value3");

        Optional<Object> result = cache.get("key1", "name");

        assertThat(result).isEmpty();
    }

    @Test
    void putShouldUpdateWhenEntryIsAlreadyPresent() {
        String expectedValue = "new-value2";
        Cache<String, Object> cache = new LRUCache(2);

        cache.put("key1", "name", "value1");
        cache.put("key2", "name", "value2");
        cache.put("key2", "name", expectedValue);
        Optional<Object> actualValue = cache.get("key2", "name");

        assertThat(actualValue).isPresent().contains(expectedValue);
    }
}