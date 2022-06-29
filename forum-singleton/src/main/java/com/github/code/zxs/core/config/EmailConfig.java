package com.github.code.zxs.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "bbs.auth.email")
public class EmailConfig {
    /**
     * 邮件服务商域名
     */
    private String host;
    /**
     * 邮件服务商端口
     */
    private String port;
    /**
     * 发件人账号
     */
    private String account;
    /**
     * 发件人名称
     */
    private String accountName;
    /**
     * 发件人密码
     */
    private String password;
}
