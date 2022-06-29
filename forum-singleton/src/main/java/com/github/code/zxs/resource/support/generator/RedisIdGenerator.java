package com.github.code.zxs.resource.support.generator;

import com.github.code.zxs.resource.support.counter.CacheCounter;
import org.springframework.stereotype.Component;

@Component
public class RedisIdGenerator implements DistributedIdGenerator {
    private final CacheCounter counter;

    public RedisIdGenerator(CacheCounter counter) {
        this.counter = counter;
    }

    @Override
    public long getLongId(String key) {
        return counter.incr(key);
    }
}
