package com.github.code.zxs.core.auth.config;







@Getter
@Setter
@ConditionalOnProperty(prefix = "global.config.core.task.", name = "core")
@Configuration
@ConfigurationProperties(prefix = "global.config.auth")
public class AuthConfig {

    /**
     * PC端最大登录设备数，默认为 5
     */
    private Integer pcMaxDevice = 5;
    /**
     * 移动端最大登录设备数，默认为 1
     */
    private Integer mobileMaxDevice = 1;

    public static final String LOGIN_INFO_PREFIX = "auth:login:";

    public static String getLoginInfoKey(String userId) {
        return LOGIN_INFO_PREFIX + userId;
    }
}
