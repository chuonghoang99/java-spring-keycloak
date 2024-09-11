package com.devteria.profile.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.devteria.profile.service.BaseRedisService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BaseRedisServiceImpl implements BaseRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, String value) {

        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setTimeToLive(String key, long timeoutInDays) {

        redisTemplate.expire(key, timeoutInDays, TimeUnit.DAYS);
    }

    @Override
    public void hashSet(String key, String field, Object value) {

        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, field, value);
    }

    @Override
    public boolean hashExists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<String, Object> getField(String key) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();

        return hashOperations.entries(key);
    }

    @Override
    public Object hashGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public List<Object> hashGetByFieldPrefix(String key, String fieldPrefix) {

        //        List<Object> objects = new ArrayList<>();
        //
        //        HashOperations<String, String, Object> hashOperations =
        //                redisTemplate.opsForHash();
        //
        //        Map<String, Object> hasEntries = hashOperations.entries(key);
        //
        //        for (Map.Entry<String, Object> entry : hasEntries.entrySet()) {
        //            if (entry.getKey().startsWith(fieldPrefix)) {
        //                objects.add(entry.getValue());
        //            }
        //        }

        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();

        return hashOperations.entries(key).entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(fieldPrefix))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public Set<String> getFieldPrefixes(String key) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key).keySet();
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    @Override
    public void delete(String key, List<String> fields) {

        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();

        hashOperations.delete(key, fields);
    }
}
