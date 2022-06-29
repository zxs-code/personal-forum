package com.github.code.zxs.core.util;


import java.util.UUID;

public class UUIDUtils {
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String randomCompactUUID() {
        return randomUUID().replaceAll("-", "");
    }

    public static String shortUUID() {
        return randomUUID(6);
    }

    public static String randomUUID(int length) {
        return randomCompactUUID().substring(0, length);
    }
}
