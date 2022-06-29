package com.github.code.zxs.resource.model.bo;


import com.github.code.zxs.core.model.bo.BaseBO;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyBO extends BaseBO {
    private Long id;
    private Long commentId;
    private Long dialogId;
    private String content;
    private Long likes;
    private Long dislikes;
    private AuthorBO from;
    private AuthorBO to;
    private Date createTime;
    private LikeStateEnum likeState;
}
