package com.github.code.zxs.core.config;

import com.github.code.zxs.core.model.enums.MessageQueueEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bbs.core.message")
public class MessageConfig {
    private MessageQueueEnum instance;
}
