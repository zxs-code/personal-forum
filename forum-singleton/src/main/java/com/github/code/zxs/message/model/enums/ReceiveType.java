package com.github.code.zxs.message.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReceiveType implements BaseEnum {
    PUSH(0, "推送"),
    PULL(1, "拉取");

    @EnumValue
    private final int code;
    private final String value;

    @JsonCreator
    public static ReceiveType valueOf(int code) {
        return BaseEnum.valueOf(ReceiveType.class, code);
    }
}
