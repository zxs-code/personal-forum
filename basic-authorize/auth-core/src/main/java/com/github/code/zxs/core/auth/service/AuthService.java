package com.github.code.zxs.core.auth.service;


import com.github.code.zxs.core.auth.dto.LoginInfoDTO;
import com.github.code.zxs.core.auth.dto.TokenInfoDTO;
import com.github.code.zxs.core.auth.dto.TokenUserDTO;

import java.util.List;

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
