package com.github.code.zxs.auth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 手机或社交账号类型
 */
@AllArgsConstructor
@Getter
public enum  AccountType {
    EMAIL("邮箱"),
    PHONE("手机号码");
    private String description;
}
