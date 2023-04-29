package ru.clevertec.newsresource.cache;

import java.util.Optional;

public interface Cache<K, V> {
    void put(K key, String name, V value);

    Optional<V> get(K key, String name);
}
