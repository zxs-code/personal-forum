package com.github.code.zxs.core.aspect;

import com.github.code.zxs.core.annotation.RateLimit;
import com.github.code.zxs.core.generator.base.KeyGenerator;
import com.github.code.zxs.core.exception.RateLimitException;
import com.github.code.zxs.core.model.dto.RedisCellResult;
import com.github.code.zxs.core.model.enums.RateLimitStrategyEnum;
import com.github.code.zxs.core.model.enums.ResponseStatusEnum;
import com.github.code.zxs.core.util.RedisCellUtils;
import com.github.code.zxs.core.util.SpelUtils;
import com.github.code.zxs.core.util.SpringContextUtils;
import com.github.code.zxs.core.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class RateLimitAspect {

    @Before(value = "@annotation(rateLimit)")
    public void before(JoinPoint joinPoint, RateLimit rateLimit) {
        if (rateLimit.strategy() == RateLimitStrategyEnum.BEFORE)
            limit(joinPoint, rateLimit);
    }

    @After(value = "@annotation(rateLimit)")
    public void after(JoinPoint joinPoint, RateLimit rateLimit) {
        if (rateLimit.strategy() == RateLimitStrategyEnum.AFTER)
            limit(joinPoint, rateLimit);
    }

    @AfterReturning(value = "@annotation(rateLimit)")
    public void afterReturning(JoinPoint joinPoint, RateLimit rateLimit) {
        if (rateLimit.strategy() == RateLimitStrategyEnum.AFTER_RETURNING)
            limit(joinPoint, rateLimit);
    }

    @AfterThrowing(value = "@annotation(rateLimit)")
    public void afterThrowing(JoinPoint joinPoint, RateLimit rateLimit) {
        if (rateLimit.strategy() == RateLimitStrategyEnum.AFTER_THROWING)
            limit(joinPoint, rateLimit);
    }


    private void limit(JoinPoint joinPoint, RateLimit rateLimit) {
        String key = "";
        Class<? extends KeyGenerator> generatorClass = rateLimit.keyGenerator();
        //如果配置key的构造器，将忽略注解的key属性
        if (generatorClass != KeyGenerator.class) {
            KeyGenerator keyGenerator = SpringContextUtils.getBean(generatorClass);
            key = rateLimit.prefix() + keyGenerator.get(joinPoint.getTarget().getClass(),
                    joinPoint.getSignature(),
                    joinPoint.getArgs());
        } else {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Object target = joinPoint.getTarget();
            Object[] args = joinPoint.getArgs();
            key = rateLimit.prefix() + ":" + method.getName() + ":" + (
                    StringUtils.isEmpty(rateLimit.key())
                            ? "[" + StringUtils.join(args, ',') + "]"
                            : SpelUtils.parse(target, rateLimit.key(), method, args)
            );
        }

        long maxBurst = rateLimit.maxBurst();
        long countPerPeriod = rateLimit.countPerPeriod();
        long period = rateLimit.unit().toSeconds(rateLimit.period());
        long quantity = rateLimit.quantity();

        RedisCellResult redisCellResult = RedisCellUtils.redisCell(key, maxBurst, countPerPeriod, period, quantity);
        if (!Boolean.TRUE.equals(redisCellResult.getAllow()))
            throw new RateLimitException(
                    ResponseStatusEnum.REQUEST_FREQUENCY.getCode(),
                    StringUtils.isEmpty(rateLimit.message()) ? ResponseStatusEnum.REQUEST_FREQUENCY.getMsg() : rateLimit.message(),
                    key,
                    redisCellResult);
    }

}
