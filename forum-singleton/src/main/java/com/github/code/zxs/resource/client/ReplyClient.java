package com.github.code.zxs.resource.client;

import com.github.code.zxs.resource.model.bo.ReplyBO;
import com.github.code.zxs.resource.service.biz.base.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 改为远端调用 TODO
 */
@Component
public class ReplyClient {
    @Autowired
    private ReplyService replyService;

    public ReplyBO getReplyById(@PathVariable Long id) {
        return replyService.getReplyDetail(id);
    }
}
