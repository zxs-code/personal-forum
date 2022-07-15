package com.github.code.zxs.message.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageStatus implements BaseEnum {
    UNREAD(0, "未读"),
    READ(1, "已读"),
    RECALL(2, "已被撤回"),
    DELETED(3, "已删除"),
    IGNORE(4, "不再通知");

    @EnumValue
    private final int code;
    private final String value;

    @JsonCreator
    public static MessageStatus valueOf(int code) {
        return BaseEnum.valueOf(MessageStatus.class, code);
    }
}
