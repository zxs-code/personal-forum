package com.github.code.zxs.resource.client;

import com.github.code.zxs.resource.model.bo.CommentBO;
import com.github.code.zxs.resource.service.biz.base.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 改为远端调用 TODO
 */
@Component
public class CommentClient {
    @Autowired
    private CommentService commentService;


    @GetMapping("{id}")
    public CommentBO getCommentDetail(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }
}
