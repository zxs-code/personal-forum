package com.github.code.zxs.resource.model.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventTypeEnum implements BaseEnum {

    LIKE(0, "点赞"),
    DISLIKE(1, "点踩"),
    COLLECT(2, "收藏");

    private final int code;
    private final String description;


    @JsonCreator
    public static EventTypeEnum valueOf(int code) {
        return BaseEnum.valueOf(EventTypeEnum.class, code);
    }

}
