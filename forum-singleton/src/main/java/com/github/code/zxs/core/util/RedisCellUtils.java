package com.github.code.zxs.core.util;

import com.github.code.zxs.core.exception.BaseException;
import com.github.code.zxs.core.model.dto.RedisCellResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Big
 * @since https://github.com/brandur/redis-cell
 * CL.THROTTLE <key> <max_burst> <count per period> <period> [<quantity>]
 * <pre>
 * 127.0.0.1:6379> CL.THROTTLE user123 15 30 60
 * 1) (integer) 0
 * 2) (integer) 16
 * 3) (integer) 15
 * 4) (integer) -1
 * 5) (integer) 2
 * 返回数组项的含义是：
 * 1. 动作是否受限：
 *   0 表示允许该操作。
 *   1 表示该操作受到限制/阻止。
 * 2. 密钥的总限制 (max_burst+1)。这相当于常见的X-RateLimit-Limit HTTP返回头。
 * 3. 密钥的剩余限制。相当于X-RateLimit-Remaining。
 * 4. 用户应重试之前的秒数，-1如果允许操作，则始终如此。相当于Retry-After。
 * 5. 限制将重置为其最大容量之前的秒数。相当于X-RateLimit-Reset。
 * </pre>
 */
@Component
@Slf4j
public final class RedisCellUtils {

    /**
     * lua 脚本
     */
    public static final String LUA_SCRIPT = "return redis.call('cl.throttle',KEYS[1], ARGV[1], ARGV[2], ARGV[3], ARGV[4])";

    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisCellUtils(RedisTemplate<String, Object> template) {
        redisTemplate = template;
    }


    /**
     * @param key            key值
     * @param maxBurst       漏斗的大小 最大爆发
     * @param countPerPeriod 周期内增加的令牌个数
     * @param period         周期，单位秒
     * @param quantity       申请令牌数
     * @return
     */
    public static RedisCellResult redisCell(String key, long maxBurst, long countPerPeriod, long period, long quantity) {
        try {
            DefaultRedisScript<List> script = new DefaultRedisScript<>(LUA_SCRIPT, List.class);
            List<Long> rst = redisTemplate.execute(script, Collections.singletonList(key), maxBurst, countPerPeriod, period, quantity);
            return new RedisCellResult(rst.get(0) == 0, rst.get(1), rst.get(2), rst.get(3), rst.get(4));
            /*
             * 1. 动作是否受限：
             *   0 表示允许该操作。
             *   1 表示该操作受到限制/阻止。
             * 2. 密钥的总限制 (max_burst+1)。这相当于常见的X-RateLimit-Limit HTTP返回头。
             * 3. 密钥的剩余限制。相当于X-RateLimit-Remaining。
             * 4. 用户应重试之前的秒数，-1如果允许操作，则始终如此。相当于Retry-After。
             * 5. 限制将重置为其最大容量之前的秒数。相当于X-RateLimit-Reset。
             */
            //这里只关注第一个元素0表示正常，1表示过载
        } catch (Exception e) {
            log.error("限流{}获取失败：", key, e);
            throw new BaseException("系统繁忙，请稍后再试");
        }
    }

}
 