package com.github.code.zxs.resource.model.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户行为
 */
@Getter
@AllArgsConstructor
public enum ActionEnum implements BaseEnum {

    /**
     * 点赞
     */
    LIKE(0, "like"),
    /**
     * 点踩
     */
    DISLIKE(1, "dislike"),
    /**
     * 收藏
     */
    COLLECT(2, "collect"),
    /**
     * 回复
     */
    REPLY(3, "reply"),
    /**
     * @
     */
    AT(4, "at")
    ;

    @EnumValue
    private final int code;
    private final String value;

    @JsonCreator
    public static ActionEnum valueOf(int code) {
        return BaseEnum.valueOf(ActionEnum.class, code);
    }

}
