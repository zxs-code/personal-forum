package com.github.code.zxs.message.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SendType implements BaseEnum {
    /**
     * 接收者只有1个
     */
    UNICAST(0, "单播"),
    /**
     * 接收者有多个
     */
    MULTICAST(1, "多播"),
    /**
     * 接收者为全体用户
     */
    BROADCAST(2, "广播");

    @EnumValue
    private final int code;
    private final String value;

    @JsonCreator
    public static SendType valueOf(int code) {
        return BaseEnum.valueOf(SendType.class, code);
    }

}
