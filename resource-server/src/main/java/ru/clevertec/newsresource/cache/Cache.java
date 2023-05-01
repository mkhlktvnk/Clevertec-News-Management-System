package ru.clevertec.newsresource.cache;

import java.util.Optional;

public interface Cache<K, V> {
    void put(K key, String prefix, V value);

    Optional<V> get(K key, String prefix);

    void evict(K key, String prefix);
}
