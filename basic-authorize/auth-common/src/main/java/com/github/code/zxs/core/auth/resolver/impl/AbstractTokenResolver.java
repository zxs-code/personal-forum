package com.github.code.zxs.core.auth.resolver.impl;






@Slf4j
public abstract class AbstractTokenResolver implements TokenResolver {
    public void init() {
        log.info("token解析器正在初始化");
    }

    public void after() {
        log.info("token解析器加载完毕：{}", this.getClass().getSimpleName());
    }

    @Override
    public TokenInfoDTO resolve(String token) {
        TokenInfoDTO tokenInfo;
        try {
            tokenInfo = getTokenInfo(token);
        } catch (Exception e) {
            log.warn("解析令牌出错", e);
            return null;
        }
        if (tokenInfo == null || tokenInfo.getType() != TokenType.ACCESS_TOKEN) {
            log.warn("非访问令牌，无法解析");
            return null;
        }
        tokenInfo.setToken(token);
        return tokenInfo;
    }

    protected abstract TokenInfoDTO getTokenInfo(String token);
}
