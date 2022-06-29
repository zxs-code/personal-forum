package com.github.code.zxs.core.annotation;

import com.github.code.zxs.core.generator.base.KeyGenerator;
import com.github.code.zxs.core.model.enums.LockFailStrategyEnum;
import com.github.code.zxs.core.model.enums.LockWaitStrategyEnum;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁,切面 {@link com.github.code.zxs.core.aspect.LockAspect}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Lock {
    /**
     * key的前缀
     */
    String prefix() default "";

    /**
     * 默认为 方法名和方法参数组合, 使用spel表达式。
     * 如果配置了KeyGenerator，将忽略此属性
     */
    String key() default "";

    /**
     * key的构造器,使用时需要注入Spring容器
     */
    Class<? extends KeyGenerator> keyGenerator() default KeyGenerator.class;

    /**
     * 访问被拒绝时的提示消息，仅在失败策略为 {@link LockFailStrategyEnum#THROW_EXCEPTION} 有效
     *
     * @return
     */
    String message() default "";

    /**
     * 锁过期时间, 等待策略为 {@link LockWaitStrategyEnum#TIME_WAIT} 时生效
     *
     * @return
     */
    long leaseTime() default 30;


    /**
     * 无法马上获取锁时的等待时间，等待策略为 {@link LockWaitStrategyEnum#TIME_WAIT} 时生效
     *
     * @return
     */
    long waitTime() default 10;

    /**
     * 时间单位，默认为秒
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 无法获取锁时的等待策略
     *
     * @return
     */
    LockWaitStrategyEnum waitStrategy() default LockWaitStrategyEnum.FAST_FAIL;

    /**
     * 获取失败时的策略
     */
    LockFailStrategyEnum failStrategy() default LockFailStrategyEnum.THROW_EXCEPTION;
}
