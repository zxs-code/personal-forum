package com.github.code.zxs.auth.model.vo;


import lombok.Data;

@Data
public class LoginInfoVO {
    private String id;
    /**
     * 用户登录时间
     */
    private String loginTime;
    /**
     * 用户登录时的ip地址
     */
    private String ip;
    /**
     * 用户登录ip对应城市
     */
    private String city;
    /**
     * 用户登录ip运营商
     */
    private String isp;
}
