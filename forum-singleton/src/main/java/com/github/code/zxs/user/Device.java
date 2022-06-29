package com.github.code.zxs.user;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 */
@AllArgsConstructor
@Getter
public enum Device {
    PC("PC端"),
    ANDROID("安卓客户端"),
    IOS("苹果客户端"),
    UNKNOWN("未知设备");

    String name;
}
