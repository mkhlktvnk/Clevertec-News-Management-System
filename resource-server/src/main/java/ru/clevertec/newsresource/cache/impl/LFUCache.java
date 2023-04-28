package ru.clevertec.newsresource.cache.impl;

import ru.clevertec.newsresource.cache.Cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LFUCache implements Cache<String, Object> {
    private final int MAX_ENTRIES;
    private final Map<String, Object> cache;
    private final Map<String, Integer> usageCount;
    private final Map<Integer, LinkedHashSet<String>> frequencyList;
    private int minFrequency = 0;

    public LFUCache(int capacity) {
        MAX_ENTRIES = capacity;
        cache = new HashMap<>(capacity);
        usageCount = new HashMap<>(capacity);
        frequencyList = new HashMap<>();
        frequencyList.put(1, new LinkedHashSet<>());
    }

    @Override
    public void put(String key, Object value) {
        if (cache.containsKey(key)) {
            cache.put(key, value);
            get(key);
            return;
        }

        if (cache.size() == MAX_ENTRIES) {
            String evictionCandidate = frequencyList.get(minFrequency).iterator().next();
            frequencyList.get(minFrequency).remove(evictionCandidate);
            cache.remove(evictionCandidate);
            usageCount.remove(evictionCandidate);
        }

        cache.put(key, value);
        usageCount.put(key, 1);
        frequencyList.get(1).add(key);
        minFrequency = 1;
    }

    @Override
    public Object get(String key) {
        if (!cache.containsKey(key)) {
            return null;
        }

        int count = usageCount.get(key);
        usageCount.put(key, count + 1);
        frequencyList.get(count).remove(key);
        if (count == minFrequency && frequencyList.get(count).isEmpty()) {
            minFrequency++;
            frequencyList.remove(count);
        }
        frequencyList.putIfAbsent(count + 1, new LinkedHashSet<>());
        frequencyList.get(count + 1).add(key);
        return cache.get(key);
    }
}
