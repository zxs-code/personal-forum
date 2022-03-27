package com.github.code.zxs.core.auth.service.impl;
















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
    public TokenInfoDTO getTokenInfo(String accessToken) {
        return getTokenInfo(accessToken, TokenType.ACCESS_TOKEN);
    }

    protected TokenInfoDTO getTokenInfo(String token, TokenType tokenType) {
        TokenInfoDTO tokenInfo;
        try {
            tokenInfo = resolveToken(token);
        } catch (Exception e) {
            log.warn("解析令牌出错", e);
            return null;
        }
        if (tokenInfo == null || (tokenType != null && tokenInfo.getType() != tokenType)) {
            log.warn("令牌类型错误，无法解析,令牌类型：{}", tokenType);
            return null;
        }
        tokenInfo.setToken(token);
        return tokenInfo;
    }

    protected abstract TokenInfoDTO resolveToken(String token);

    @Override
    public TokenUserDTO getUserInfo(String accessToken) {
        return getTokenInfo(accessToken).getUserDTO();
    }

    @Override
    public TokenType getTokenType(String accessToken) {
        return getTokenInfo(accessToken).getType();
    }

    @Override
    public Date getExpiration(String accessToken) {
        return getTokenInfo(accessToken).getExpiration();
    }

    @Override
    public TokenInfoDTO refreshToken(String refreshToken) {
        TokenInfoDTO tokenInfoDTO = getTokenInfo(refreshToken, TokenType.REFRESH_TOKEN);
        return createAccessToken(tokenInfoDTO.getUserDTO());
    }
}
