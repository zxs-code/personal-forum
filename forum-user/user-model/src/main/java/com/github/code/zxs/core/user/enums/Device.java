package com.github.code.zxs.core.user.enums;




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
