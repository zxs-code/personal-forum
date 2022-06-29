package com.github.code.zxs.core.aspect;

import com.github.code.zxs.core.annotation.Lock;
import com.github.code.zxs.core.exception.LockException;
import com.github.code.zxs.core.generator.base.KeyGenerator;
import com.github.code.zxs.core.util.*;
import com.google.common.base.Defaults;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class LockAspect {
    @Autowired
    private RedissonClient redissonClient;

    private static final String LOCK_PREFIX = "lock";

    @Around(value = "@annotation(lock)")
    public Object around(ProceedingJoinPoint point, Lock lock) throws Throwable {
        String key = "";
        Class<? extends KeyGenerator> generatorClass = lock.keyGenerator();
        MethodSignature signature = (MethodSignature) point.getSignature();

        //如果配置key的构造器，将忽略注解的key属性
        if (generatorClass != KeyGenerator.class) {
            KeyGenerator keyGenerator = SpringContextUtils.getBean(generatorClass);
            key = RedisUtil.getSimpleCacheKey(
                    LOCK_PREFIX,
                    lock.prefix(),
                    keyGenerator.get(point.getTarget().getClass(), signature, point.getArgs()));
        } else {
            Method method = signature.getMethod();
            Object target = point.getTarget();
            Object[] args = point.getArgs();
            key = RedisUtil.getSimpleCacheKey(
                    LOCK_PREFIX,
                    lock.prefix(),
                    StringUtils.isEmpty(lock.key())
                            ? method.getName() + "[" + StringUtils.join(args, ',') + "]"
                            : SpelUtils.parse(target, lock.key(), method, args));
        }

        RLock rLock = redissonClient.getLock(key);
        boolean success = false;
        try {
            switch (lock.waitStrategy()) {
                case BLOCK:
                    rLock.lock();
                    success = true;
                    break;
                case FAST_FAIL:
                    success = rLock.tryLock();
                    break;
                case TIME_WAIT:
                    success = rLock.tryLock(lock.waitTime(), lock.leaseTime(), lock.unit());
                    break;
            }
        } catch (Exception e) {
            rLock.unlock();
            throw e;
        }

        if (!success) {
            switch (lock.failStrategy()) {
                case THROW_EXCEPTION:
                    throw new LockException(StringUtils.isNotEmpty(lock.message()) ? lock.message() : "获取分布式锁失败", key);
                case RETURN_DEFAULT:
                    Class<?> returnType = signature.getReturnType();
                    return returnType.isPrimitive() ? Defaults.defaultValue(returnType) : null;
            }
        }
        try {
            return point.proceed();
        } finally {
            rLock.unlock();
        }
    }
}

