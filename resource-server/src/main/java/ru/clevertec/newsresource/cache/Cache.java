package ru.clevertec.newsresource.cache;

import java.util.Optional;


/**
 * An interface representing a cache that stores key-value pairs with the ability to put, get and evict items.
 * @param <K> the type of keys in the cache
 * @param <V> the type of values in the cache
 */
public interface Cache<K, V> {

    /**
     * Puts the given key-value pair in the cache with a specified prefix.
     * If the cache is at capacity, the eviction strategy specified by the implementation will determine which item is evicted.
     * @param key the key of the item to be put in the cache
     * @param prefix the prefix to be added to the key for identification purposes
     * @param value the value of the item to be put in the cache
     */
    void put(K key, String prefix, V value);

    /**
     * Retrieves the value associated with the given key and prefix in the cache if it exists.
     * If the item is found, its access count is incremented according to the eviction strategy specified by the implementation.
     * @param key the key of the item to be retrieved from the cache
     * @param prefix the prefix associated with the key
     * @return an Optional containing the value associated with the key and prefix, or empty if not found in the cache
     */
    Optional<V> get(K key, String prefix);

    /**
     * Removes the item associated with the given key and prefix from the cache if it exists.
     * @param key the key of the item to be evicted from the cache
     * @param prefix the prefix associated with the key
     */
    void evict(K key, String prefix);
}

