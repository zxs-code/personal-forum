package com.github.code.zxs.resource.service.biz.base;

import com.github.code.zxs.resource.model.bo.CommentDataBO;
import com.github.code.zxs.resource.model.dto.CommentAddDTO;
import com.github.code.zxs.resource.model.dto.CommentViewDTO;

public interface CommentService {

    void addComment(CommentAddDTO commentAddDTO);

    CommentDataBO listComment(CommentViewDTO commentViewDTO);
}
