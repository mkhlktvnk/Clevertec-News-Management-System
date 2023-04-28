package ru.clevertec.newsresource.cache.impl;

import ru.clevertec.newsresource.cache.Cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUCache implements Cache<String, Object> {
    private Map<String, Object> cache;
    private LinkedList<String> keys;
    private final Integer maxEntries;

    public LRUCache(Integer capacity) {
        this.cache = new HashMap<>(capacity);
        this.keys = new LinkedList<>();
        this.maxEntries = capacity;
    }

    @Override
    public void put(String key, Object value) {
        if (cache.containsKey(key)) {
            keys.remove(key);
        }
        cache.put(key, value);
        keys.addFirst(key);
        if (keys.size() > maxEntries) {
            String last = keys.removeLast();
            cache.remove(last);
        }
    }

    @Override
    public Object get(String key) {
        Object value = cache.get(key);
        if (value != null) {
            keys.remove(key);
            keys.addFirst(key);
        }
        return value;
    }
}
