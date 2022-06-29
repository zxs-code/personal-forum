package com.github.code.zxs.core.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LockFailStrategyEnum {
    THROW_EXCEPTION("抛出异常"),
    RETURN_DEFAULT("返回默认值，对象返回null，基本数据类型返回默认值");
    private final String description;
}
