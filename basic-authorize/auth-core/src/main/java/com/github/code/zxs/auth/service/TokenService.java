package com.github.code.zxs.auth.service;

import com.github.code.zxs.auth.dto.TokenInfoDTO;
import com.github.code.zxs.auth.dto.TokenUserDTO;
import com.github.code.zxs.auth.enums.TokenType;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface TokenService {
    /**
     * 创建访问令牌
     *
     * @param userDTO 保存的用户信息
     * @param expire  有效时长
     * @param unit    有效时长单位
     * @return
     */
    TokenInfoDTO createAccessToken(TokenUserDTO userDTO, int expire, TimeUnit unit);

    /**
     * 创建刷新令牌
     *
     * @param userDTO
     * @param expire  有效时长
     * @param unit    有效时长单位
     * @return
     */
    TokenInfoDTO createRefreshToken(TokenUserDTO userDTO, int expire, TimeUnit unit);

    /**
     * 创建访问令牌
     *
     * @param userDTO
     * @return
     */
    TokenInfoDTO createAccessToken(TokenUserDTO userDTO);

    /**
     * 创建刷新令牌
     *
     * @param userDTO
     * @return
     */
    TokenInfoDTO createRefreshToken(TokenUserDTO userDTO);
    /**
     * 验证令牌是否有效
     *
     * @param token
     * @return
     */
    boolean validate(String token);

    /**
     * 获取令牌信息
     *
     * @param token
     * @return
     */
    TokenInfoDTO getTokenInfo(String token);

    /**
     * 获取令牌存储的用户信息
     *
     * @param token
     * @return
     */
    TokenUserDTO getUserInfo(String token);

    /**
     * 获取令牌类型
     *
     * @param token
     * @return
     */
    TokenType getTokenType(String token);

    /**
     * 获取令牌过期时间
     *
     * @param token
     * @return
     */
    Date getExpiration(String token);

    /**
     * 移除某个令牌
     *
     * @param token
     * @return
     */
    boolean removeToken(String token);

    /**
     * 移除参数中所有令牌
     *
     * @param tokens
     * @return
     */
    boolean removeToken(List<String> tokens);

    /**
     * 根据refreshToken重新获取accessToken
     *
     * @param refreshToken
     * @return accessToken
     */
    TokenInfoDTO refreshToken(String refreshToken);
}
