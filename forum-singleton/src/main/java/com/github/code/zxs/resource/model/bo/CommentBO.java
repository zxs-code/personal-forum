package com.github.code.zxs.resource.model.bo;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentBO {
    private Long id;
    private Long seq;
    private ResourceTypeEnum resourceType;
    private Long resourceId;
    private String content;
    private Long likes;
    private Long dislikes;
    private Long reply;
    private LikeStateEnum likeState;
    private Date createTime;

    private AuthorBO author;
    private List<ReplyBO> replies;
}
