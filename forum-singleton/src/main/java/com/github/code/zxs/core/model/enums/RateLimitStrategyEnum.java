package com.github.code.zxs.core.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RateLimitStrategyEnum {
    BEFORE("执行方法前触发"),
    AFTER_RETURNING("方法正常执行后触发"),
    AFTER_THROWING("方法执行异常触发"),
    AFTER("方法执行后触发，无论方法是否执行成功");
    private final String description;
}
