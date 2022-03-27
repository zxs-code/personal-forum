package com.github.code.zxs.core.auth.service;







public interface AuthService {
    /**
     * 登录
     *
     * @param tokenUserDTO
     * @return
     */
    LoginInfoDTO login(TokenUserDTO tokenUserDTO);

    /**
     * 登出
     *
     * @param token
     * @return
     */
    Boolean logout(String token);

    Boolean logout(List<String> token);

    Boolean logout(LoginInfoDTO loginInfoDTO);

    TokenInfoDTO getTokenInfoDTO(String accessToken);
}
