package com.github.code.zxs.auth.resolver.impl;

import com.github.code.zxs.auth.config.UUIDTokenConfig;
import com.github.code.zxs.auth.dto.TokenInfoDTO;
import com.github.code.zxs.auth.enums.TokenType;
import com.github.code.zxs.core.exception.BaseException;
import com.github.code.zxs.core.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * UUID解析器
 */
@Component
@ConditionalOnBean(UUIDTokenConfig.class)
public class UUIDTokenResolver extends AbstractTokenResolver {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UUIDTokenConfig uuidTokenConfig;

    public UUIDTokenResolver(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    protected TokenInfoDTO getTokenInfo(String token, TokenType tokenType) {
        String cacheKey = uuidTokenConfig.getTokenCacheKey(token);
        return Optional.ofNullable(redisUtil.get(cacheKey, TokenInfoDTO.class))
                .orElseThrow(() -> new BaseException("缓存中无此token"));
    }

}
