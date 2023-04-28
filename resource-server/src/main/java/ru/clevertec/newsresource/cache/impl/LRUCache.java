package ru.clevertec.newsresource.cache.impl;

import ru.clevertec.newsresource.cache.Cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUCache implements Cache<String, Object> {
    private final int MAX_ENTRIES;
    private final Map<String, Object> cache;
    private final LinkedList<String> keys;

    public LRUCache(Integer capacity) {
        this.cache = new HashMap<>(capacity);
        this.keys = new LinkedList<>();
        this.MAX_ENTRIES = capacity;
    }

    @Override
    public void put(String key, Object value) {
        if (cache.containsKey(key)) {
            keys.remove(key);
        }
        cache.put(key, value);
        keys.addFirst(key);
        if (keys.size() > MAX_ENTRIES) {
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
