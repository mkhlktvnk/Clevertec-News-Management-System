package ru.clevertec.newsresource.cache;

public interface Cache<K, V> {
    void put(K key, String name, V value);

    V get(K key, String name);
}
