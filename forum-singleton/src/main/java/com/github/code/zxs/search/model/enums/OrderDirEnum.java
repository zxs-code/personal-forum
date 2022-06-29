package com.github.code.zxs.search.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderDirEnum implements BaseEnum {
    ASC(0,"ASC"),
    DESC(1,"DESC");

    @EnumValue
    private final int code;
    private final String value;

    @JsonCreator
    public static OrderDirEnum valueOf(int code) {
        return BaseEnum.valueOf(OrderDirEnum.class, code);
    }
}
