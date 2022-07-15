package com.github.code.zxs.resource.consumer;

import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.support.counter.ResourceUniqueVisitorCounter;
import com.github.code.zxs.resource.model.bo.UserViewDTO;
import com.github.code.zxs.resource.model.entity.*;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import com.github.code.zxs.resource.service.manager.PostsDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PostsConsumer {

    @Autowired
    private PostsDataManager postsDataManager;

    private final ResourceUniqueVisitorCounter uvCounter;

    public PostsConsumer(RedisTemplate<String, Object> redisTemplate) {
        this.uvCounter = new ResourceUniqueVisitorCounter(redisTemplate, ResourceTypeEnum.POSTS);
    }

    @KafkaListener(topics = TopicConstant.POSTS_LIKE, groupId = "cache", containerFactory = "batchFactory")
    public void updateLikeCount(List<Like> likes) {
        Map<Long, Map<LikeStateEnum, Long>> countMap = likes.stream().collect(Collectors.groupingBy(Like::getResourceId, Collectors.groupingBy(Like::getState, Collectors.counting())));
        countMap.forEach((id, map) -> {
            long likeCount = map.getOrDefault(LikeStateEnum.LIKE, 0L) - map.getOrDefault(LikeStateEnum.CANCEL_LIKE, 0L);
            long dislikeCount = map.getOrDefault(LikeStateEnum.DISLIKE, 0L) - map.getOrDefault(LikeStateEnum.CANCEL_DISLIKE, 0L);
            postsDataManager.incr(id, PostsData.Fields.likes, likeCount);
            postsDataManager.incr(id, PostsData.Fields.dislikes, dislikeCount);
        });
    }

    @KafkaListener(topics = TopicConstant.POSTS_COMMENT, groupId = "cache", containerFactory = "batchFactory")
    public void updateCommentCount(List<Comment> comments) {
        comments.stream()
                .collect(Collectors.groupingBy(Comment::getResourceId, Collectors.counting()))
                .forEach((id, count) -> postsDataManager.incr(id, PostsData.Fields.comment, count));
    }

    @KafkaListener(topics = TopicConstant.POSTS_REPLY, groupId = "cache", containerFactory = "batchFactory")
    public void updateReplyCount(List<Reply> replies) {
        replies.stream()
                .collect(Collectors.groupingBy(Reply::getResourceId, Collectors.counting()))
                .forEach((id, count) -> postsDataManager.incr(id, PostsData.Fields.comment, count));
    }

    @KafkaListener(topics = TopicConstant.POSTS_COLLECT, groupId = "cache", containerFactory = "batchFactory")
    public void updateStarsCount(List<Collect> collects) {
        Map<Long, Map<Boolean, Long>> countMap = collects.stream()
                .collect(Collectors.groupingBy(Collect::getResourceId,
                        Collectors.groupingBy(Collect::getState, Collectors.counting())));
        countMap.forEach((id, map) -> {
            long collectCount = map.getOrDefault(true, 0L) - map.getOrDefault(false, 0L);
            postsDataManager.incr(id, PostsData.Fields.stars, collectCount);
        });
    }

    @KafkaListener(topics = TopicConstant.USER_VIEW, groupId = "cache", containerFactory = "batchFactory")
    public void updateViewCount(List<UserViewDTO> userViewDTOs) {
        Map<Long, List<Long>> resourceIdUserIdMap = userViewDTOs.stream()
                .filter(userViewDTO -> userViewDTO.getResourceDTO().getType() == ResourceTypeEnum.POSTS)
                .collect(Collectors.groupingBy(userViewDTO -> userViewDTO.getResourceDTO().getId(), Collectors.mapping(UserViewDTO::getUserId, Collectors.toList())));
        resourceIdUserIdMap.forEach((resourceId, userIds) -> {
            postsDataManager.incr(resourceId, PostsData.Fields.views, userIds.size());
            if (uvCounter.incr(resourceId, userIds.toArray())) {
                postsDataManager.put(resourceId, PostsData.Fields.visitor, uvCounter.get(resourceId));
            }
        });
    }
}
