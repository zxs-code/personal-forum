package com.github.code.zxs.resource.model.dto;

import lombok.Data;

@Data
public class ReplyAddDTO {
    private Long commentId;
    private Long toReplyId;
    private String content;
}
