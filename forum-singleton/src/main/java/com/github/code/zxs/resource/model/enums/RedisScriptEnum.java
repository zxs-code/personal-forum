package com.github.code.zxs.resource.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisScriptEnum implements BaseEnum {
    LIKE(0,"like", Long.class);

    private final int code;
    private final String name;
    private final Class<?> returnType;

    @JsonCreator
    public static RedisScriptEnum valueOf(int code) {
        return BaseEnum.valueOf(RedisScriptEnum.class, code);
    }
}
