package com.github.code.zxs.resource.converter;

import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.core.util.StringUtils;
import com.github.code.zxs.resource.model.bo.ReplyBO;
import com.github.code.zxs.resource.model.dto.ReplyAddDTO;
import com.github.code.zxs.resource.model.entity.Reply;
import com.github.code.zxs.resource.support.generator.DistributedIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ReplyConverter {
    @Autowired
    private DistributedIdGenerator idGenerator;
    private static final String REPLY_ID_KEY = "comment-id-counter";
    private static final String REPLY_SEQ_KEY = "comment-seq-counter";


    public Reply addDto2Entity(ReplyAddDTO addDTO) {
        Date date = new Date();
        Reply reply = BeanUtils.copy(addDTO, new Reply());
        reply.setId(idGenerator.getLongId(REPLY_ID_KEY));
        reply.setSeq(idGenerator.getLongId(StringUtils.join(":", REPLY_SEQ_KEY, addDTO.getCommentId())));
        reply.setCreateBy(UserContext.getId());
        reply.setCreateTime(date);
        reply.setUpdateTime(date);
        reply.setLikes(0L);
        reply.setDislikes(0L);
        return reply;
    }


    public ReplyBO entity2ReplyBo(Reply reply) {
        return BeanUtils.copy(reply, new ReplyBO());
    }
}
