package com.github.code.zxs.resource.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.github.code.zxs.resource.model.bo.ReplyBO;
import com.github.code.zxs.resource.model.bo.ReplyDataBO;
import com.github.code.zxs.resource.model.dto.DialogViewDTO;
import com.github.code.zxs.resource.model.dto.ReplyAddDTO;
import com.github.code.zxs.resource.model.dto.ReplyViewDTO;
import com.github.code.zxs.resource.service.biz.base.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("reply")
public class ReplyController {
    @Autowired
    private ReplyService replyService;

    @SaCheckLogin
    @PostMapping
    public void addComment(@Valid @RequestBody ReplyAddDTO replyAddDTO) {
        replyService.addReply(replyAddDTO);
    }

    @PostMapping("list")
    public ReplyDataBO listReply(@Validated @RequestBody ReplyViewDTO replyViewDTO) {
        replyViewDTO.setCount(20L);
        return replyService.rangeReply(replyViewDTO);
    }

    @PostMapping("dialog/list")
    public ReplyDataBO listDialog(@Validated @RequestBody DialogViewDTO dialogViewDTO) {
        dialogViewDTO.setCount(20L);
        return replyService.rangeDialog(dialogViewDTO);
    }

    @GetMapping("{id}")
    public ReplyBO getReplyById(@PathVariable Long id) {
        return replyService.getReplyDetail(id);
    }
}
