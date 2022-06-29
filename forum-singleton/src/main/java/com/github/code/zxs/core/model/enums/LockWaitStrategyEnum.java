package com.github.code.zxs.core.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LockWaitStrategyEnum {
    BLOCK("阻塞等待"),
    FAST_FAIL("快速失败"),
    TIME_WAIT("等待一段时间");
    private final String description;
}
