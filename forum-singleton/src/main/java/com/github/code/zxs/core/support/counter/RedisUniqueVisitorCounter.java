package com.github.code.zxs.core.support.counter;

import com.github.code.zxs.core.util.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;

public class RedisUniqueVisitorCounter {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUniqueVisitorCounter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public long get(String... keys) {
        return redisTemplate.opsForHyperLogLog().size(keys);
    }

    public boolean incr(String key, Object... userId) {
        String[] values = Arrays.stream(userId).map(Object::toString).toArray(String[]::new);
        return redisTemplate.opsForHyperLogLog().add(key, values) == 1;
    }

    public long reset(String... keys) {
        return redisTemplate.delete(CollectionUtils.asList(keys));
    }
}
