package com.github.code.zxs.core.util;

import org.slf4j.helpers.MessageFormatter;

import java.util.Arrays;
import java.util.stream.Stream;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 拼接字符串
     *
     * @param delimiter 分隔符
     * @param elements
     * @return
     */
    public static String join(CharSequence delimiter, Object... elements) {
        return String.join(delimiter,
                Arrays.stream(elements).map(Object::toString).toArray(String[]::new));
    }

    /**
     * 字符串脱敏，将索引start到end-1的字符变为*
     *
     * @param src
     * @param start
     * @param end
     * @return
     */
    public static String desensitization(String src, int start, int end) {
        if (StringUtils.isEmpty(src)) {
            return "";
        }
        char[] chars = src.toCharArray();
        for (int i = start; i < end; i++)
            chars[i] = '*';
        return new String(chars);
    }

    public static String format(String messagePattern, Object... args) {
        return MessageFormatter.arrayFormat(messagePattern, args, null).getMessage();
    }

    public static void main(String[] args) {
        Double[] objects = Stream.generate(Math::random).limit(10000000).toArray(Double[]::new);
        int count = 1000000;
        System.out.println(TestUtils.costTime(count, (Double temp) -> "12345" + temp + "12345" + temp + "1" + temp + "1" + temp, objects).toMillis());
        System.out.println(TestUtils.costTime(count, (Double temp) -> format("12345{}12345{}1{}1{}", temp, temp, temp, temp), objects).toMillis());
    }
}
