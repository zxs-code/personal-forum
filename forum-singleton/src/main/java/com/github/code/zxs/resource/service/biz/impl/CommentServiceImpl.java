package com.github.code.zxs.resource.service.biz.impl;

import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.component.CommentCursor;
import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.support.message.MessageProducer;
import com.github.code.zxs.resource.converter.CommentConverter;
import com.github.code.zxs.resource.model.bo.AuthorBO;
import com.github.code.zxs.resource.model.bo.CommentBO;
import com.github.code.zxs.resource.model.bo.CommentDataBO;
import com.github.code.zxs.resource.model.bo.ReplyDataBO;
import com.github.code.zxs.resource.model.dto.CommentAddDTO;
import com.github.code.zxs.resource.model.dto.CommentViewDTO;
import com.github.code.zxs.resource.model.dto.ReplyViewDTO;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.entity.Comment;
import com.github.code.zxs.resource.model.enums.CommentOrderEnum;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import com.github.code.zxs.resource.service.biz.base.CommentService;
import com.github.code.zxs.resource.service.biz.base.LikeService;
import com.github.code.zxs.resource.service.biz.base.ReplyService;
import com.github.code.zxs.resource.service.biz.base.UserInfoService;
import com.github.code.zxs.resource.service.manager.CommentManager;
import com.github.code.zxs.resource.service.manager.ReplyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentConverter commentConverter;
    @Autowired
    private MessageProducer producer;
    @Autowired
    private CommentManager commentManager;
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ReplyManager replyManager;
    @Autowired
    private ReplyService replyService;

    @Override
    public void addComment(CommentAddDTO commentAddDTO) {
        Comment comment = commentConverter.addDto2Entity(commentAddDTO);
        producer.asyncSend(TopicConstant.POSTS_COMMENT, comment.getCreateBy().toString(), comment);
    }


    @Override
    public CommentDataBO listComment(CommentViewDTO commentViewDTO) {

        ResourceDTO resourceDTO = commentViewDTO.getResource();
        CommentOrderEnum orderBy = commentViewDTO.getOrderBy();
        long start = commentViewDTO.getNext();
        long end = commentViewDTO.getCount() + start - 1;

        long commentCount = commentManager.countComment(resourceDTO);
        long replyCount = replyManager.countReply(resourceDTO);
        long total = commentCount + replyCount;
        List<Comment> comments = commentManager.listComment(resourceDTO, orderBy, start, end);

        List<CommentBO> list = comments.stream()
                .map(commentConverter::entity2CommentBo)
                .collect(Collectors.toList());
        List<ResourceDTO> resourceDTOs = comments.stream().map(e -> new ResourceDTO(e.getId(), ResourceTypeEnum.COMMENT)).collect(Collectors.toList());
        List<Long> userIds = comments.stream().map(Comment::getCreateBy).collect(Collectors.toList());
        List<Long> commentIds = comments.stream().map(Comment::getId).collect(Collectors.toList());

        List<Long> likeCount = likeService.count(resourceDTOs, LikeStateEnum.LIKE);
        List<Long> dislikeCount = likeService.count(resourceDTOs, LikeStateEnum.DISLIKE);
        List<AuthorBO> authorBOs = userInfoService.listAuthorBo(userIds);

        for (int i = 0; i < list.size(); i++) {
            CommentBO commentBO = list.get(i);
            commentBO.setLikes(likeCount.get(i));
            commentBO.setDislikes(dislikeCount.get(i));
            //待优化 TODO
            ReplyDataBO replyDataBO = replyService.rangeReply(new ReplyViewDTO(commentBO.getId(), 0L, 3L));
            long replyTotal = replyDataBO.getCursor().getTotal();
            commentBO.setReply(replyTotal);
            commentBO.setReplies(replyDataBO.getReplies());
            commentBO.setAuthor(authorBOs.get(i));
        }
        if (UserContext.getId() != null) {
            Long curUserId = UserContext.getId();
            List<LikeStateEnum> likeStates = likeService.userLikeState(curUserId, ResourceTypeEnum.COMMENT, commentIds);

            for (int i = 0; i < list.size(); i++) {
                CommentBO commentBO = list.get(i);
                commentBO.setLikeState(likeStates.get(i));
            }
        }

        end = start + list.size();
        CommentCursor cursor = new CommentCursor(total, start == 0, end >= commentCount, start - 1, end + 1, orderBy);
        return new CommentDataBO(cursor, list);
    }

}
