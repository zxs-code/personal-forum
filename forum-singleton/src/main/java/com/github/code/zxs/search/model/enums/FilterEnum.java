package com.github.code.zxs.search.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.code.zxs.auth.model.enums.UserStateEnum;
import com.github.code.zxs.core.model.enums.BaseEnum;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.search.model.document.PostsDocument;
import com.github.code.zxs.search.model.document.UserDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilterEnum implements BaseEnum {
    /**
     * 帖子过滤
     */
    GOOD_POSTS(0, ResourceTypeEnum.POSTS, PostsDocument.Fields.good, OperatorEnum.EQUALS, true, null, false, "加精的帖子"),
    LOCKED_POSTS(1, ResourceTypeEnum.POSTS, PostsDocument.Fields.locked, OperatorEnum.EQUALS, true, null, false, "锁定的帖子"),
    NOT_HIDE_POSTS(2, ResourceTypeEnum.POSTS, PostsDocument.Fields.hide, OperatorEnum.EQUALS, false, null, true, "非隐藏的帖子"),
    EXISTS_POSTS(3, ResourceTypeEnum.POSTS, PostsDocument.Fields.deleteTime, OperatorEnum.EQUALS, null, null, true, "存在的帖子"),

    /**
     * 用户过滤
     */
    NOT_DEACTIVATE_USER(20,ResourceTypeEnum.USER, UserDocument.Fields.state, OperatorEnum.NOT_EQUALS, UserStateEnum.DEACTIVATE,null,true,"未注销的用户");

    @EnumValue
    private final int code;
    private final ResourceTypeEnum type;
    private final String fieldName;
    private final OperatorEnum operatorEnum;
    private final Object v1;
    private final Object v2;
    private final boolean defaultOption;
    private final String description;

    @JsonCreator
    public static FilterEnum valueOf(int code) {
        return BaseEnum.valueOf(FilterEnum.class, code);
    }
}
