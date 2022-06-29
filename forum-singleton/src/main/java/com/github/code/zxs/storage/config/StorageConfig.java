package com.github.code.zxs.storage.config;

import com.github.code.zxs.core.util.DateUtils;
import com.github.code.zxs.storage.model.enums.ContentTypeEnum;
import com.github.code.zxs.storage.model.enums.StorageTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Date;

@Configuration
@ConfigurationProperties(prefix = "bbs.storage")
@Data
public class StorageConfig {
    public static final String SEPARATOR = "/";
    public static final String FILE_PREFIX = "file/";
    /**
     * 存储类型
     */
    private StorageTypeEnum type;
    /**
     * 文件url前缀
     */
    private String baseUrl;
    /**
     * 允许上传的文件类型
     */
    private ContentTypeEnum[] allowContentType;

    @PostConstruct
    public void init() {
        if (!baseUrl.endsWith(SEPARATOR))
            baseUrl += SEPARATOR;
        baseUrl += FILE_PREFIX;
    }

    public String getBaseUrlWithDate(Date date) {
        return baseUrl + DateUtils.date(date, SEPARATOR) + SEPARATOR;
    }
}
