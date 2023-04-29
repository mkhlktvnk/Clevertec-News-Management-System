package ru.clevertec.newsresource.cache.impl;

import ru.clevertec.newsresource.cache.Cache;

import java.util.HashMap;
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
    public void put(String key, String name, Object value) {
        this.lock.writeLock().lock();
        try {
            if (capacity == 0) {
                return;
            }
            if (valueMap.containsKey(key)) {
                valueMap.put(key, value);
                return;
            }
            if (valueMap.size() >= capacity) {
                String keyToEvict = frequencyMap.get(minUsed).iterator().next();
                frequencyMap.get(minUsed).remove(keyToEvict);
                countMap.remove(keyToEvict);
                valueMap.remove(keyToEvict);
            }
            minUsed = 1;
            valueMap.put(key, value);
            countMap.put(key, 1L);
            frequencyMap.get(minUsed).add(key);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<Object> get(String key, String name) {
        this.lock.readLock().lock();
        try {
            if (!valueMap.containsKey(key)) {
                return Optional.empty();
            }
            long count = countMap.get(key);
            countMap.put(key, count + 1);
            frequencyMap.get(count).remove(key);
            if (count == minUsed && frequencyMap.get(count).size() == 0) {
                minUsed++;
            }
            if (!frequencyMap.containsKey(count + 1)) {
                frequencyMap.put(count + 1, new LinkedHashSet<>());
            }
            frequencyMap.get(count + 1).add(key);
            return Optional.of(valueMap.get(key));
        } finally {
            this.lock.readLock().unlock();
        }
    }
}

