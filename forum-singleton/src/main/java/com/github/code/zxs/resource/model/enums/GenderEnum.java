package com.github.code.zxs.resource.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum implements BaseEnum {
    MALE(0, "男"),
    FEMALE(1, "女"),
    SECRET(2, "保密");

    @EnumValue
    private final int code;
    private final String description;

    @JsonCreator
    public static GenderEnum valueOf(int code) {
        return BaseEnum.valueOf(GenderEnum.class, code);
    }
}
