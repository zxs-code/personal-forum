package com.github.code.zxs.auth.service.impl;

import com.github.code.zxs.auth.config.UUIDTokenConfig;
import com.github.code.zxs.auth.dto.TokenInfoDTO;
import com.github.code.zxs.core.util.RedisUtil;
import com.github.code.zxs.core.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@ConditionalOnBean(UUIDTokenConfig.class)
public class UUIDTokenService extends AbstractTokenService {
    @Autowired
    private RedisUtil redisUtil;


    @Override
    protected String createToken(TokenInfoDTO tokenInfoDTO) {
        String token = UUIDUtils.randomCompactUUID();
        redisUtil.set(UUIDTokenConfig.getTokenCacheKey(token), tokenInfoDTO);
        return token;
    }

    @Override
    public boolean removeToken(List<String> tokens) {
        List<String> keyList = tokens.stream().map(UUIDTokenConfig::getTokenCacheKey).collect(Collectors.toList());
        return redisUtil.delete(keyList) == tokens.size();
    }

}
