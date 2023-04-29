package ru.clevertec.newsresource.cache.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import ru.clevertec.newsresource.cache.Cache;

import java.util.Optional;

@RequiredArgsConstructor
public class RedisCache implements Cache<String, Object> {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void put(String key, String name, Object value) {
        redisTemplate.opsForValue().set(name + ":" + key, value);
    }

    @Override
    public Optional<Object> get(String key, String name) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(name + ":" + key));
    }
}

