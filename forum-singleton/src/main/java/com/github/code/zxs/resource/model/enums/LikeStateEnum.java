package com.github.code.zxs.resource.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LikeStateEnum implements BaseEnum {
    LIKE(0, "点赞"),
    CANCEL_LIKE(1, "取消点赞"),
    DISLIKE(2, "点踩"),
    CANCEL_DISLIKE(3, "取消点踩"),
    NEVER(4, "还未进行过点赞点踩操作");

    @EnumValue
    private final int code;
    private final String description;

    @JsonCreator
    public static LikeStateEnum valueOf(int code) {
        return BaseEnum.valueOf(LikeStateEnum.class, code);
    }

    @Override
    public int getCode() {
        return code;
    }
}
