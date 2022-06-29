package com.github.code.zxs.search.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.core.model.enums.BaseEnum;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.search.model.document.PostsDocument;
import com.github.code.zxs.search.model.document.UserDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderEnum implements BaseEnum {
    LATEST_PUBLISH(0, ResourceTypeEnum.POSTS, PostsDocument.Fields.createTime, OrderDirEnum.DESC, "最新发布"),
    MOST_VIEWS(1, ResourceTypeEnum.POSTS, PostsDocument.Fields.views, OrderDirEnum.DESC, "最多观看"),
    MOST_COMMENT(2, ResourceTypeEnum.POSTS, PostsDocument.Fields.comment, OrderDirEnum.DESC, "最多评论"),
    MOST_LIKES(3, ResourceTypeEnum.POSTS, PostsDocument.Fields.likes, OrderDirEnum.DESC, "最多点赞"),
    MOST_FANS(10, ResourceTypeEnum.USER, UserDocument.Fields.fans, OrderDirEnum.DESC, "粉丝数由高到低"),
    LEAST_FANS(11, ResourceTypeEnum.USER, UserDocument.Fields.fans, OrderDirEnum.ASC, "粉丝数由低到高");

    @EnumValue
    private final int code;
    private final ResourceTypeEnum type;
    private final String orderBy;
    private final OrderDirEnum orderDir;
    private final String description;

    @JsonCreator
    public static OrderEnum valueOf(int code) {
        return BaseEnum.valueOf(OrderEnum.class, code);
    }
}
