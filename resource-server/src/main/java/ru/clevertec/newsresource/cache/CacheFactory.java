package ru.clevertec.newsresource.cache;

import lombok.NoArgsConstructor;
import ru.clevertec.newsresource.cache.impl.LFUCache;
import ru.clevertec.newsresource.cache.impl.LRUCache;

@NoArgsConstructor(staticName = "getInstance")
public class CacheFactory {

    public Cache<String, Object> getCache(String evictStrategy, int capacity) {
        if (evictStrategy.equals(EvictStrategy.LRU)) {
            return new LRUCache(capacity);
        } else if (evictStrategy.equals(EvictStrategy.LFU)) {
            return new LFUCache(capacity);
        }
        throw new IllegalArgumentException("Unknown evict strategy: " + evictStrategy);
    }

}
