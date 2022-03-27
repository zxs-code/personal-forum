package com.github.code.zxs.core.auth.service;









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
     * 获取访问令牌信息
     *
     * @param accessToken
     * @return
     */
    TokenInfoDTO getTokenInfo(String accessToken);

    /**
     * 获取访问令牌存储的用户信息
     *
     * @param accessToken
     * @return
     */
    TokenUserDTO getUserInfo(String accessToken);

    /**
     * 获取令牌类型
     *
     * @param accessToken
     * @return
     */
    TokenType getTokenType(String accessToken);

    /**
     * 获取令牌过期时间
     *
     * @param accessToken
     * @return
     */
    Date getExpiration(String accessToken);

    /**
     * 移除某个令牌
     *
     * @param token
     * @return
     */
    Boolean removeToken(String token);

    /**
     * 移除参数中所有令牌
     *
     * @param tokens
     * @return
     */
    Boolean removeToken(List<String> tokens);

    /**
     * 根据refreshToken重新获取accessToken
     *
     * @param refreshToken
     * @return accessToken
     */
    TokenInfoDTO refreshToken(String refreshToken);
}
