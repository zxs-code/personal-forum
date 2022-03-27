package com.github.code.zxs.core.auth.vo;




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
    /**
     * 用户登录时的设备类型
     */
    private Device device;
}
