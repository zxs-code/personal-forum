package com.github.code.zxs.resource.service.biz.impl;

import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.component.Cursor;
import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.core.exception.UserEventException;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.support.message.MessageProducer;
import com.github.code.zxs.core.util.CollectionUtils;
import com.github.code.zxs.resource.converter.ReplyConverter;
import com.github.code.zxs.resource.model.bo.AuthorBO;
import com.github.code.zxs.resource.model.bo.ReplyBO;
import com.github.code.zxs.resource.model.bo.ReplyDataBO;
import com.github.code.zxs.resource.model.dto.DialogViewDTO;
import com.github.code.zxs.resource.model.dto.ReplyAddDTO;
import com.github.code.zxs.resource.model.dto.ReplyViewDTO;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.entity.Comment;
import com.github.code.zxs.resource.model.entity.Reply;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import com.github.code.zxs.resource.service.biz.base.LikeService;
import com.github.code.zxs.resource.service.biz.base.ReplyService;
import com.github.code.zxs.resource.service.biz.base.UserInfoService;
import com.github.code.zxs.resource.service.manager.CommentManager;
import com.github.code.zxs.resource.service.manager.ReplyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private MessageProducer producer;
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ReplyManager replyManager;
    @Autowired
    private CommentManager commentManager;
    @Autowired
    private ReplyConverter replyConverter;

    @Override
    public void addReply(ReplyAddDTO replyAddDTO) {

        Reply reply = replyConverter.addDto2Entity(replyAddDTO);
        Comment comment = commentManager.getCommentById(reply.getCommentId());
        if (comment == null)
            throw new UserEventException("回复的评论不存在", UserContext.getId());

        //冗余资源主体（帖子）
        reply.setResourceType(comment.getResourceType());
        reply.setResourceId(comment.getResourceId());

        //设置对话id
        Long toReplyId = replyAddDTO.getToReplyId();
        if (toReplyId != null) {
            Reply toReply = replyManager.listReply(Collections.singletonList(toReplyId)).get(0);
            reply.setToUserId(toReply.getCreateBy());
            reply.setDialogId(toReply.getDialogId());
        } else {
            reply.setDialogId(reply.getId());
        }
        producer.asyncSend(TopicConstant.POSTS_REPLY, reply.getCreateBy().toString(), reply);
    }

    @Override
    public ReplyDataBO rangeReply(ReplyViewDTO replyViewDTO) {
        long commentId = replyViewDTO.getCommentId();
        long start = replyViewDTO.getNext();
        long end = replyViewDTO.getCount() + start - 1;
        List<ReplyBO> list = listReply(commentId, start, end);
        long replyCount = replyManager.countReply(commentId);
        Cursor cursor = new Cursor(replyCount, start == 0, end >= replyCount, start - 1, end + 1);

        return new ReplyDataBO(cursor, list);
    }

    @Override
    public ReplyDataBO rangeDialog(DialogViewDTO replyViewDTO) {
        long commentId = replyViewDTO.getCommentId();
        long dialogId = replyViewDTO.getDialogId();
        long start = replyViewDTO.getNext();
        long end = replyViewDTO.getCount() + start - 1;
        List<Long> ids = replyManager.listReplyIdByDialogId(dialogId, start, end);
        List<ReplyBO> replies = wrapperReply(replyManager.listReply(ids));
        Long replyCount = replyManager.countDialogReply(dialogId);

        Cursor cursor = new Cursor(replyCount, start == 0, end >= replyCount, start - 1, end + 1);
        return new ReplyDataBO(cursor, replies);
    }

    @Override
    public List<ReplyBO> listReply(long commentId, long start, long end) {
        List<Reply> replies = replyManager.listReply(commentId, start, end);
        return wrapperReply(replies);
    }

    @Override
    public ReplyBO getReplyDetail(Long replyId) {
        Reply reply = replyManager.getById(replyId);
        return wrapperReply(CollectionUtils.asList(reply)).get(0);
    }

    private List<ReplyBO> wrapperReply(List<Reply> replies) {
        List<ReplyBO> list = replies.stream().map(replyConverter::entity2ReplyBo).collect(Collectors.toList());

        List<ResourceDTO> resourceDTOs = replies.stream().map(e -> new ResourceDTO(e.getId(), ResourceTypeEnum.REPLY)).collect(Collectors.toList());
        List<Long> replyIds = replies.stream().map(Reply::getId).collect(Collectors.toList());
        List<Long> fromIds = replies.stream().map(Reply::getCreateBy).collect(Collectors.toList());
        List<Long> toIds = replies.stream().map(Reply::getToUserId).filter(Objects::nonNull).collect(Collectors.toList());
        ArrayList<Long> userIds = new ArrayList<>(fromIds);
        userIds.addAll(toIds);

        List<Long> likeCount = likeService.count(resourceDTOs, LikeStateEnum.LIKE);
        List<Long> dislikeCount = likeService.count(resourceDTOs, LikeStateEnum.DISLIKE);
        List<AuthorBO> authorBOs = userInfoService.listAuthorBo(userIds);

        int index = fromIds.size();
        for (int i = 0; i < list.size(); i++) {
            ReplyBO replyBO = list.get(i);
            replyBO.setLikes(likeCount.get(i));
            replyBO.setDislikes(dislikeCount.get(i));
            replyBO.setFrom(authorBOs.get(i));

            if (replies.get(i).getToUserId() != null)
                replyBO.setTo(authorBOs.get(index++));
        }

        if (UserContext.getId() != null) {
            long curUserId = UserContext.getId();
            List<LikeStateEnum> likeStates = likeService.userLikeState(curUserId, ResourceTypeEnum.REPLY, replyIds);
            for (int i = 0; i < list.size(); i++) {
                ReplyBO replyBO = list.get(i);
                replyBO.setLikeState(likeStates.get(i));
            }
        }
        return list;
    }
}
