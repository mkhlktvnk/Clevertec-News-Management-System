package ru.clevertec.newsresource.cache.impl;

import ru.clevertec.newsresource.cache.Cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class LRUCache implements Cache<String, Object> {
    private final int MAX_ENTRIES;
    private final Map<String, Object> cache;
    private final Map<String, LinkedList<String>> keys;

    public LRUCache(Integer capacity) {
        this.cache = new HashMap<>(capacity);
        this.keys = new HashMap<>();
        this.MAX_ENTRIES = capacity;
    }

    @Override
    public void put(String key, String name, Object value) {
        String cacheKey = name + ":" + key;
        if (cache.containsKey(cacheKey)) {
            keys.get(name).remove(key);
        }
        cache.put(cacheKey, value);
        keys.putIfAbsent(name, new LinkedList<>());
        keys.get(name).addFirst(key);
        if (keys.get(name).size() > MAX_ENTRIES) {
            String last = keys.get(name).removeLast();
            cache.remove(name + ":" + last);
        }
    }

    @Override
    public Optional<Object> get(String key, String name) {
        String cacheKey = name + ":" + key;
        Object value = cache.get(cacheKey);
        if (value != null) {
            keys.get(name).remove(key);
            keys.get(name).addFirst(key);
        }
        return Optional.ofNullable(value);
    }

}
