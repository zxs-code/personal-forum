package com.github.code.zxs.resource.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentOrderEnum implements BaseEnum {
    HOT(0, "按热门"),
    TIME(1, "按时间"),
    VIEW(2, "按请求量");
    private final int code;
    private final String description;

    @JsonCreator
    public static CommentOrderEnum valueOf(int code) {
        return BaseEnum.valueOf(CommentOrderEnum.class, code);
    }
}
