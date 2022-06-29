package com.github.code.zxs.resource.converter;

import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.core.util.StringUtils;
import com.github.code.zxs.resource.model.bo.CommentBO;
import com.github.code.zxs.resource.model.dto.CommentAddDTO;
import com.github.code.zxs.resource.model.entity.Comment;
import com.github.code.zxs.resource.support.generator.DistributedIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CommentConverter {
    @Autowired
    private DistributedIdGenerator idGenerator;
    public static final String COMMENT_ID_KEY = "comment-id-counter";
    public static final String COMMENT_SEQ_KEY = "comment-seq-counter";

    public Comment addDto2Entity(CommentAddDTO addDTO) {
        Date date = new Date();
        Comment comment = BeanUtils.copy(addDTO, new Comment());
        comment.setId(idGenerator.getLongId(COMMENT_ID_KEY));
        comment.setSeq(idGenerator.getLongId(StringUtils.join(":", COMMENT_SEQ_KEY, addDTO.getResourceType(), addDTO.getResourceId())));
        comment.setCreateBy(UserContext.getId());
        comment.setCreateTime(date);
        comment.setUpdateTime(date);
        comment.setLikes(0L);
        comment.setDislikes(0L);
        comment.setReply(0L);
        return comment;
    }


    public CommentBO entity2CommentBo(Comment comment) {
        return BeanUtils.copy(comment, new CommentBO());
    }
}
