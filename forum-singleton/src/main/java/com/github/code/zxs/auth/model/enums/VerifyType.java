package com.github.code.zxs.auth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码校验类型
 */
@AllArgsConstructor
@Getter
public enum VerifyType {
    LOGIN("登录验证"),
    REGISTER("注册验证"),
    MODIFY_PASSWORD("修改密码验证");
    private final String description;
}
