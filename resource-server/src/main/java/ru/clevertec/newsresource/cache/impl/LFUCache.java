package ru.clevertec.newsresource.cache.impl;

import ru.clevertec.newsresource.cache.Cache;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An implementation of a cache using the LFU (Least Frequently Used) eviction strategy.
 * Keeps track of the usage frequency of each key and evicts the least frequently used
 * items when the capacity is exceeded.
 */
public class LFUCache implements Cache<String, Object> {
    private final int capacity;
    private final Map<String, Object> valueMap;
    private final Map<String, Long> countMap;
    private final Map<Long, LinkedHashSet<String>> frequencyMap;
    private long minUsed = -1;
    private final ReentrantReadWriteLock lock;

    /**
     * Constructs an empty LFUCache with the specified capacity.
     *
     * @param capacity  the maximum number of entries that this cache can hold
     */
    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.valueMap = new ConcurrentHashMap<>();
        this.countMap = new ConcurrentHashMap<>();
        this.frequencyMap = new ConcurrentHashMap<>();
        this.frequencyMap.put(1L, new LinkedHashSet<>());
        this.lock = new ReentrantReadWriteLock();
    }

    /**
     * Associates the specified value with the specified key in this cache.
     * If the cache previously contained a mapping for the key, the old value
     * is replaced. If the cache is full, the least frequently used entry is
     * evicted to make room for the new entry.
     *
     * @param key      the key with which the specified value is to be associated
     * @param prefix   the prefix to prepend to the key
     * @param value    the value to be associated with the specified key
     */
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

    /**
     * Returns the value to which the specified key is mapped, or an empty
     * Optional if this cache contains no mapping for the key. If the key is
     * found, its usage count is incremented by 1, and the entry is moved to
     * the next higher frequency bucket. If the entry was the only one in the
     * lowest frequency bucket and has just been moved to a higher bucket, the
     * minimum frequency is incremented by 1.
     *
     * @param key      the key whose associated value is to be returned
     * @param prefix   the prefix to prepend to the key
     * @return         an Optional containing the value to which the specified key is mapped,
     *                 or an empty Optional if this cache contains no mapping for the key
     */
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

    /**
     * Removes the mapping for the specified key from this cache if present.
     * If the key is found, its entry is removed from the frequency map and
     * usage count map. If the entry was the only one in the lowest frequency
     * bucket and has just been removed, the minimum frequency is adjusted
     * accordingly.
     *
     * @param key      the key whose mapping is to be removed from the cache
     * @param prefix   the prefix to prepend to the key
     */
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

