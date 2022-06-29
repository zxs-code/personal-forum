package com.github.code.zxs.storage.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StorageTypeEnum {
    LOCAL("local", "本地存储"),
    ALIYUN("aliyun", "阿里云 OSS"),
    TENCENT("tencent", "腾讯云 COS"),
    QINIU("qiniu", "七牛云 KODO");

    private final String key;
    private final String description;

}
