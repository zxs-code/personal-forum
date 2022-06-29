package com.github.code.zxs.resource.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.github.code.zxs.resource.model.bo.CommentDataBO;
import com.github.code.zxs.resource.model.dto.CommentAddDTO;
import com.github.code.zxs.resource.model.dto.CommentViewDTO;
import com.github.code.zxs.resource.service.biz.base.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @SaCheckLogin
    @PostMapping
    public void addComment(@Valid @RequestBody CommentAddDTO commentAddDTO) {
        commentService.addComment(commentAddDTO);
    }

    @PostMapping("list")
    public CommentDataBO listComment(@Validated @RequestBody CommentViewDTO commentViewDTO) {
        commentViewDTO.setCount(20L);
        return commentService.listComment(commentViewDTO);
    }

}
