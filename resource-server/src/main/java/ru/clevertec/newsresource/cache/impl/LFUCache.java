package ru.clevertec.newsresource.cache.impl;

import ru.clevertec.newsresource.cache.Cache;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LFUCache implements Cache<String, Object> {
    private final int capacity;
    private final Map<String, Object> valueMap;
    private final Map<String, Long> countMap;
    private final Map<Long, LinkedHashSet<String>> frequencyMap;
    private long minUsed = -1;
    private final ReentrantReadWriteLock lock;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.valueMap = new ConcurrentHashMap<>();
        this.countMap = new ConcurrentHashMap<>();
        this.frequencyMap = new ConcurrentHashMap<>();
        this.frequencyMap.put(1L, new LinkedHashSet<>());
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public void put(String key, String prefix, Object value) {
        this.lock.writeLock().lock();
        try {
            String cacheKey = prefix + ":" + key;
            if (capacity == 0) {
                return;
            }
            if (valueMap.containsKey(cacheKey)) {
                valueMap.put(cacheKey, value);
                return;
            }
            if (valueMap.size() >= capacity) {
                String keyToEvict = frequencyMap.get(minUsed).iterator().next();
                frequencyMap.get(minUsed).remove(keyToEvict);
                countMap.remove(keyToEvict);
                valueMap.remove(keyToEvict);
            }
            minUsed = 1;
            valueMap.put(cacheKey, value);
            countMap.put(cacheKey, 1L);
            frequencyMap.get(minUsed).add(cacheKey);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<Object> get(String key, String prefix) {
        this.lock.readLock().lock();
        try {
            String cacheKey = prefix + ":" + key;
            if (!valueMap.containsKey(cacheKey)) {
                return Optional.empty();
            }
            long count = countMap.get(cacheKey);
            countMap.put(cacheKey, count + 1);
            frequencyMap.get(count).remove(cacheKey);
            if (count == minUsed && frequencyMap.get(count).size() == 0) {
                minUsed++;
            }
            if (!frequencyMap.containsKey(count + 1)) {
                frequencyMap.put(count + 1, new LinkedHashSet<>());
            }
            frequencyMap.get(count + 1).add(cacheKey);
            return Optional.of(valueMap.get(cacheKey));
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public void evict(String key, String prefix) {
        this.lock.writeLock().lock();
        try {
            String cacheKey = prefix + ":" + key;
            if (!valueMap.containsKey(cacheKey)) {
                return;
            }
            long count = countMap.get(cacheKey);
            frequencyMap.get(count).remove(cacheKey);
            if (count == minUsed && frequencyMap.get(minUsed).size() == 0) {
                frequencyMap.remove(countMap.get(cacheKey));
                minUsed = frequencyMap.keySet().stream().min(Long::compare).get();
            }
            countMap.remove(cacheKey);
            valueMap.remove(cacheKey);
        } finally {
            this.lock.writeLock().unlock();
        }
    }
}

