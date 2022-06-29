package com.github.code.zxs.auth.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStateEnum implements BaseEnum {
    NORMAL(0, "正常"),
    DISABLE(1, "已禁用"),
    DEACTIVATE(2, "已注销");
    @EnumValue
    private final int code;
    private final String description;

    @JsonCreator
    public static UserStateEnum valueOf(int code) {
        return BaseEnum.valueOf(UserStateEnum.class, code);
    }

    @Override
    public int getCode() {
        return 0;
    }
}
