package ru.clevertec.newsresource.cache.impl;

import ru.clevertec.newsresource.cache.Cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
