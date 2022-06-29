package com.github.code.zxs.storage.config;

import com.github.code.zxs.core.util.DateUtils;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Date;

@Data
@Configuration
@ConfigurationProperties(prefix = "bbs.storage.local")
@ConditionalOnProperty(prefix = "bbs:storage", name = "type", havingValue = "local", matchIfMissing = true)
public class LocalStorageConfig {

    public static final String SEPARATOR = "/";
    /**
     * 本地存储目录
     */
    private String basePath;

    @PostConstruct
    public void init() {
        if (!basePath.endsWith("\\") && !basePath.endsWith(SEPARATOR))
            basePath += SEPARATOR;
    }

    public String getBasePathWithDate(Date date) {
        return basePath + DateUtils.date(date, SEPARATOR) + SEPARATOR;
    }
}
