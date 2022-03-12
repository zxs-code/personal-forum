package com.github.code.zxs.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ResponseStatus {
    SUCCESS(0, "执行成功"),
    FAIL(1, "执行失败"),
    NOT_FOUND(1001,"资源未找到"),
    UNAUTHORIZED(1002,"未授权，请重新登录"),
    FORBIDDEN(1003,"禁止访问");



    private int code;
    private String msg;
}
