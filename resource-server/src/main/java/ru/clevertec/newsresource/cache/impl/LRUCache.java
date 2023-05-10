package ru.clevertec.newsresource.cache.impl;

import ru.clevertec.newsresource.cache.Cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An implementation of a Least Recently Used (LRU) Cache that stores key-value pairs.
 * The cache has a specified maximum capacity, and if the cache becomes full,
 * the least recently used key-value pair is evicted to make space for new entries.
 * Keys are unique within the cache and are stored as a combination of a prefix and a key.
 */
public class LRUCache implements Cache<String, Object> {
    private final int capacity;
    private final Map<String, Object> keyValueMap;
    private final Set<String> keys;
    private final ReentrantReadWriteLock lock;

    public LRUCache(Integer capacity) {
        this.capacity = capacity;
        this.keys = new HashSet<>();
        this.keyValueMap = new ConcurrentHashMap<>(capacity);
        this.lock = new ReentrantReadWriteLock();
    }

    /**
     * Adds a key-value pair to the cache. If the cache is already full, the least recently used
     * key-value pair is evicted to make space for the new entry.
     *
     * @param key the key of the key-value pair
     * @param prefix the prefix of the key for uniqueness within the cache
     * @param value the value of the key-value pair
     */
    @Override
    public void put(String key, String prefix, Object value) {
        lock.writeLock().lock();
        try {
            String cacheKey = prefix + ":" + key;
            if (this.capacity == 0) {
                return;
            }
            if (keys.contains(cacheKey)) {
                keys.remove(cacheKey);
            } else if (keys.size() == capacity) {
                String keyToEvict = keys.iterator().next();
                keys.remove(keyToEvict);
                keyValueMap.remove(keyToEvict);
            }
            keys.add(cacheKey);
            keyValueMap.put(cacheKey, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Returns the value associated with the specified key. If the key is not found in the cache,
     * an empty Optional is returned.
     *
     * @param key the key to look up
     * @param prefix the prefix of the key for uniqueness within the cache
     * @return an Optional containing the value associated with the key, or an empty Optional if
     *         the key is not found in the cache
     */
    @Override
    public Optional<Object> get(String key, String prefix) {
        lock.readLock().lock();
        try {
            String cacheKey = prefix + ":" + key;
            if (!keyValueMap.containsKey(cacheKey)) {
                return Optional.empty();
            }
            keys.remove(cacheKey);
            keys.add(cacheKey);
            return Optional.of(keyValueMap.get(cacheKey));
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Removes the key-value pair associated with the specified key from the cache.
     *
     * @param key the key of the key-value pair to remove
     * @param prefix the prefix of the key for uniqueness within the cache
     */
    @Override
    public void evict(String key, String prefix) {
        lock.writeLock().lock();
        try {
            String cacheKey = prefix + ":" + key;
            if (!keys.contains(cacheKey)) {
                return;
            }
            keys.remove(cacheKey);
            keyValueMap.remove(cacheKey);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
