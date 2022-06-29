package com.github.code.zxs.core.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源类型
 */
@Getter
@AllArgsConstructor
public enum ResourceTypeEnum implements BaseEnum {
    POSTS(0, "帖子"),
    COMMENT(1, "评论"),
    REPLY(2, "回复"),
    USER(3, "用户"),
    ROLE(4, "角色"),
    PERMISSION(5, "权限"),
    TAG(6, "标签");

    @EnumValue
    private final int code;
    private final String description;

    @JsonCreator
    public static ResourceTypeEnum valueOf(int code) {
        return BaseEnum.valueOf(ResourceTypeEnum.class, code);
    }

    @Override
    public int getCode() {
        return 0;
    }
}
