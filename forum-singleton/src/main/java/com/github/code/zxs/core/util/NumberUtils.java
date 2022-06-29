package com.github.code.zxs.core.util;

import cn.hutool.core.util.NumberUtil;

public class NumberUtils extends NumberUtil {
    /**
     * 为整数补充前导0
     * @param length 加上前导0的最大长度
     * @param num
     * @return
     */
    public static String prefixZero(int length, int num) {
        return String.format("%0" + length + "d", num);
    }
}
