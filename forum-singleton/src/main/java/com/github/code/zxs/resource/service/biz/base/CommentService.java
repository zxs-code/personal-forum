package com.github.code.zxs.resource.service.biz.base;

import com.github.code.zxs.resource.model.bo.CommentBO;
import com.github.code.zxs.resource.model.bo.CommentDataBO;
import com.github.code.zxs.resource.model.dto.CommentAddDTO;
import com.github.code.zxs.resource.model.dto.CommentViewDTO;

public interface CommentService {

    void addComment(CommentAddDTO commentAddDTO);

    CommentBO getCommentById(Long id);

    CommentDataBO listComment(CommentViewDTO commentViewDTO);
}
