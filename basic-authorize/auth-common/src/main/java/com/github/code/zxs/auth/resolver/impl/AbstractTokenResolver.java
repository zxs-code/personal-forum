package com.github.code.zxs.auth.resolver.impl;

import com.github.code.zxs.auth.dto.TokenInfoDTO;
import com.github.code.zxs.auth.enums.TokenType;
import com.github.code.zxs.auth.resolver.TokenResolver;
import com.github.code.zxs.core.enums.ResponseStatus;
import com.github.code.zxs.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTokenResolver implements TokenResolver {
    public void init() {
        log.info("token解析器正在初始化");
    }

    public void after() {
        log.info("token解析器加载完毕：{}", this.getClass().getSimpleName());
    }

    @Override
    public TokenInfoDTO resolveAccessToken(String accessToken) {
        return resolveToken(accessToken, TokenType.ACCESS_TOKEN);
    }

    @Override
    public TokenInfoDTO resolveRefreshToken(String refreshToken) {
        return resolveToken(refreshToken, TokenType.REFRESH_TOKEN);
    }

    protected TokenInfoDTO resolveToken(String token, TokenType tokenType) {
        TokenInfoDTO tokenInfo = null;
        try {
            tokenInfo = getTokenInfo(token, tokenType);
        } catch (Exception e) {
            log.warn("解析令牌出错", e);
            throw new BaseException(ResponseStatus.UNAUTHORIZED);
        }
        if (tokenInfo == null || tokenInfo.getType() != tokenType) {
            log.warn("令牌类型错误，无法解析,令牌类型：{}", tokenType);
            throw new BaseException(ResponseStatus.UNAUTHORIZED);
        }
        tokenInfo.setToken(token);
        return tokenInfo;
    }

    protected abstract TokenInfoDTO getTokenInfo(String token, TokenType tokenType);
}
