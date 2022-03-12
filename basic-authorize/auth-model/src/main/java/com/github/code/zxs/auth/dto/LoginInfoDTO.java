package com.github.code.zxs.auth.dto;

import com.github.code.zxs.auth.enums.Device;
import com.github.code.zxs.auth.enums.TokenType;
import lombok.Data;

import java.util.Date;

@Data
public class LoginInfoDTO {
    private String id;
    /**
     * 访问令牌
     */
    private String accessToken;
     /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 用户登录时间
     */
    private Date loginTime;
    /**
     * 登录失效时间，null则不会主动失效
     */
    private Date expiration;
    /**
     * token保存的用户信息
     */
    private TokenUserDTO userDTO;
    /**
     * token类型
     */
    private TokenType type;
    /**
     * 用户登录时的ip地址
     */
    private String ip;
    /**
     * 用户登录时的设备类型
     */
    private Device device;
    /**
     * 用户代理
     */
    private String userAgent;
}
