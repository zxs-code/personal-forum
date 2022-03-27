package com.github.code.zxs.core.util;



public class UUIDUtils {
    public static String randomUUID(){
        return UUID.randomUUID().toString();
    }

    public static String randomCompactUUID(){
        return randomUUID().replaceAll("-","");
    }
}
