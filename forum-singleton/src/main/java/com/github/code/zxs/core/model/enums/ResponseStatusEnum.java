package com.github.code.zxs.core.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseStatusEnum {
    SUCCESS(0, "执行成功"),
    FAIL(1, "执行失败"),
    NOT_FOUND(1001, "资源未找到"),
    UNAUTHORIZED(1002, "未授权，请登录"),
    LOGIN_FAIL(1003, "用户名或密码错误"),
    FORBIDDEN(1004, "无权限访问,请联系管理员"),
    FILE_UPLOAD_FAIL(1005, "文件上传失败"),
    FILE_NOT_FOUND(1006,"文件未找到"),
    UNSUPPORTED_FILE_TYPE(1007, "不支持该文件类型"),
    REQUEST_FREQUENCY(1008, "访问过于频繁，请稍后再试"),
    FILE_SIZE_LIMIT(1009,"上传的文件大小不能超过限制");

    private final int code;
    private final String msg;
}
