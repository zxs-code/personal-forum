package com.github.code.zxs.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConditionalOnProperty(prefix = "global.config.core.task.",name = "core")
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
}
