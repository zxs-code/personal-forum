package com.github.code.zxs.resource.service.biz.base;


import com.github.code.zxs.resource.model.bo.ReplyBO;
import com.github.code.zxs.resource.model.bo.ReplyDataBO;
import com.github.code.zxs.resource.model.dto.DialogViewDTO;
import com.github.code.zxs.resource.model.dto.ReplyAddDTO;
import com.github.code.zxs.resource.model.dto.ReplyViewDTO;

import java.util.List;

public interface ReplyService {
    void addReply(ReplyAddDTO replyAddDTO);

    ReplyDataBO rangeReply(ReplyViewDTO replyViewDTO);

    ReplyDataBO rangeDialog(DialogViewDTO replyViewDTO);

    List<ReplyBO> listReply(long commentId, long start, long end);
}
