package ru.clevertec.newsresource.cache.constant;

/**
 * The EvictStrategy class provides constants for different eviction strategies to be used by a caching system.
 * Currently, it provides support for two eviction strategies:
 *
 * <ul>
 *     <li>{@link #LFU}: Least Frequently Used</li>
 *     <li>{@link #LRU}: Least Recently Used</li>
 * </ul>
 *
 * These constants can be used to configure the caching system with the desired eviction strategy.
 */
public class EvictStrategy {

    /**
     * A constant representing the Least Frequently Used eviction strategy.
     */
    public static final String LFU = "LFU";

    /**
     * A constant representing the Least Recently Used eviction strategy.
     */
    public static final String LRU = "LRU";
}
