package com.github.code.zxs.auth.service.impl;

import com.github.code.zxs.auth.config.TokenGlobalConfig;
import com.github.code.zxs.auth.dto.TokenInfoDTO;
import com.github.code.zxs.auth.dto.TokenUserDTO;
import com.github.code.zxs.auth.enums.TokenType;
import com.github.code.zxs.auth.resolver.TokenResolver;
import com.github.code.zxs.auth.service.TokenService;
import com.github.code.zxs.core.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractTokenService implements TokenService {
    @Autowired
    protected TokenGlobalConfig tokenConfig;
    @Autowired
    protected TokenResolver tokenResolver;

    @Override
    public TokenInfoDTO createAccessToken(TokenUserDTO userDTO, int expire, TimeUnit unit) {
        return createToken(userDTO, TokenType.ACCESS_TOKEN, expire, unit);
    }

    @Override
    public TokenInfoDTO createRefreshToken(TokenUserDTO userDTO, int expire, TimeUnit unit) {
        return createToken(userDTO, TokenType.REFRESH_TOKEN, expire, unit);
    }

    @Override
    public TokenInfoDTO createAccessToken(TokenUserDTO userDTO) {
        return createAccessToken(userDTO, tokenConfig.getAccessTokenExpire(), tokenConfig.getAccessUnit());
    }

    @Override
    public TokenInfoDTO createRefreshToken(TokenUserDTO userDTO) {
        return createRefreshToken(userDTO, tokenConfig.getRefreshTokenExpire(), tokenConfig.getRefreshUnit());
    }


    /**
     * 创建令牌，封装令牌的各项信息。
     *
     * @param userDTO
     * @param type
     * @param expire
     * @param unit
     * @return
     */
    protected TokenInfoDTO createToken(TokenUserDTO userDTO, TokenType type, int expire, TimeUnit unit) {
        Date issueTime = new Date();
        Date expiration = TimeUtils.plus(issueTime, expire, unit);
        //将refreshToken过期时间设置为凌晨3点，减少用户使用过程中突然退出
        //另一种方案在refreshToken快到期时,短暂地延长有效时间 TODO
        if (type == TokenType.REFRESH_TOKEN && tokenConfig.isFriendlyExpire())
            expiration = TimeUtils.with(expiration, ChronoField.CLOCK_HOUR_OF_DAY, 3);
        TokenInfoDTO tokenInfoDTO = new TokenInfoDTO(issueTime, expiration, userDTO, type, null);
        String token = createToken(tokenInfoDTO);
        tokenInfoDTO.setToken(token);
        return tokenInfoDTO;
    }

    /**
     * 根据已有的令牌信息获取字符串形式的令牌
     *
     * @param tokenInfoDTO
     * @return
     */
    protected abstract String createToken(TokenInfoDTO tokenInfoDTO);

    @Override
    public boolean removeToken(String token) {
        return removeToken(Collections.singletonList(token));
    }

    @Override
    public TokenInfoDTO getTokenInfo(String token) {
        return tokenResolver.resolveAccessToken(token);
    }

    @Override
    public TokenUserDTO getUserInfo(String token) {
        return getTokenInfo(token).getUserDTO();
    }

    @Override
    public TokenType getTokenType(String token) {
        return getTokenInfo(token).getType();
    }

    @Override
    public Date getExpiration(String token) {
        return getTokenInfo(token).getExpiration();
    }

    @Override
    public boolean validate(String accessToken) {
        try {
            tokenResolver.resolveAccessToken(accessToken);
        } catch (Exception e) {
            log.info("token无效", e);
            return false;
        }
        return true;
    }

    @Override
    public TokenInfoDTO refreshToken(String refreshToken) {
        TokenInfoDTO tokenInfoDTO = tokenResolver.resolveRefreshToken(refreshToken);
        return createAccessToken(tokenInfoDTO.getUserDTO());
    }
}
