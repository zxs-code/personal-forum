package com.github.code.zxs.resource.support.counter;

import com.github.code.zxs.core.util.RedisUtil;
import com.github.code.zxs.core.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CacheCounter implements Counter {
    @Autowired
    private RedisUtil redisUtil;

    private static final long DEFAULT_DELTA = 1;

    private static final String DEFAULT_COUNTER_PREFIX = "counter";

    private long defaultDelta = DEFAULT_DELTA;

    private String counterPrefix = DEFAULT_COUNTER_PREFIX;

    public CacheCounter() {

    }

    public CacheCounter(long defaultDelta, String counterPrefix) {
        if (redisUtil == null)
            try {
                redisUtil = SpringContextUtils.getBean(RedisUtil.class);
            } catch (Exception e) {
                redisUtil = new RedisUtil(SpringContextUtils.getBean(RedisTemplate.class));
            }
    }

    public void setRedisTemplate(RedisTemplate<String, Object> template) {
        this.redisUtil = new RedisUtil(template);
    }

    public CacheCounter(long defaultDelta) {
        this(defaultDelta, DEFAULT_COUNTER_PREFIX);
    }

    public CacheCounter(String countPrefix) {
        this(DEFAULT_DELTA, countPrefix);
    }

    public long getDefaultDelta() {
        return defaultDelta;
    }

    public String getCounterPrefix() {
        return counterPrefix;
    }

    @Override
    public long getCount(String key) {
        Object o = redisUtil.get(key);
        if (!(o instanceof Number))
            return 0;
        return (int) o;
    }

    @Override
    public long incr(String key, long delta) {
        return redisUtil.incr(key, delta);
    }

    @Override
    public long incr(String key) {
        return redisUtil.incr(key, defaultDelta);
    }

    @Override
    public long decr(String key, long delta) {
        return redisUtil.decr(key, delta);
    }

    @Override
    public long decr(String key) {
        return redisUtil.decr(key, defaultDelta);
    }

    @Override
    public long getAndIncr(String key) {
//        redisUtil.multi();
        long newVal = incr(key);
        return newVal - 1;
    }

    @Override
    public long getAndIncr(String key, long delta) {
        long newVal = incr(key,delta);
        return newVal - delta;
    }

    @Override
    public long getAndDecr(String key) {
        long newVal = decr(key);
        return newVal + 1;
    }

    @Override
    public long getAndDecr(String key, long delta) {
        long newVal = decr(key, delta);
        return newVal - delta;
    }

    private String getCacheKey(String key) {
        return RedisUtil.getSimpleCacheKey(counterPrefix, key);
    }
}
