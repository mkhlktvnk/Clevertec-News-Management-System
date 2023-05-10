package ru.clevertec.newsresource.cache.factory;

import lombok.NoArgsConstructor;
import ru.clevertec.newsresource.cache.Cache;
import ru.clevertec.newsresource.cache.constant.EvictStrategy;
import ru.clevertec.newsresource.cache.impl.LFUCache;
import ru.clevertec.newsresource.cache.impl.LRUCache;

/**
 * The CacheFactory class provides a method to create instances of a caching system based on the provided eviction strategy
 * and capacity.
 *
 * The class uses the Factory design pattern to create objects of the required type, based on the input parameters. The
 * supported eviction strategies are {@link EvictStrategy#LRU} and {@link EvictStrategy#LFU}.
 *
 * Usage example:
 * <pre>{@code
 * CacheFactory factory = CacheFactory.getInstance();
 * Cache<String, Object> lruCache = factory.getCache(EvictStrategy.LRU, 100);
 * }</pre>
 */
@NoArgsConstructor(staticName = "getInstance")
public class CacheFactory {

    /**
     * Returns an instance of a caching system based on the provided eviction strategy and capacity.
     *
     * @param evictStrategy The eviction strategy to be used by the caching system. Supported values are {@link EvictStrategy#LRU}
     *                      and {@link EvictStrategy#LFU}.
     * @param capacity      The maximum number of items that can be stored in the cache.
     * @return An instance of the caching system.
     * @throws IllegalArgumentException If an unknown eviction strategy is provided.
     */
    public Cache<String, Object> getCache(String evictStrategy, int capacity) {
        if (evictStrategy.equals(EvictStrategy.LRU)) {
            return new LRUCache(capacity);
        } else if (evictStrategy.equals(EvictStrategy.LFU)) {
            return new LFUCache(capacity);
        }
        throw new IllegalArgumentException("Unknown evict strategy: " + evictStrategy);
    }

}
