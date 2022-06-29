package com.github.code.zxs.search.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperatorEnum implements BaseEnum {
    EQUALS(0, "等于"),
    NOT_EQUALS(1, "不等于"),
    BETWEEN(2, "在某个范围"),
    GT(3, "大于"),
    GTE(4, "大于等于"),
    LT(5, "小于"),
    LTE(6, "小于等于");

    @EnumValue
    private final int code;
    private final String value;

    @JsonCreator
    public static OperatorEnum valueOf(int code) {
        return BaseEnum.valueOf(OperatorEnum.class, code);
    }
}
