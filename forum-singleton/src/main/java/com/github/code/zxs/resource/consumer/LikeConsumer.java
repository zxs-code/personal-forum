package com.github.code.zxs.resource.consumer;

import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.support.message.MessageProducer;
import com.github.code.zxs.resource.model.entity.Like;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import com.github.code.zxs.resource.service.manager.LikeManager;
import com.github.code.zxs.resource.support.generator.DistributedIdGenerator;
import com.github.code.zxs.resource.support.result.LikeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikeConsumer {
    @Autowired
    private LikeManager likeManager;
    @Autowired
    private MessageProducer producer;
    @Autowired
    private DistributedIdGenerator idGenerator;

    @KafkaListener(topics = TopicConstant.LIKE_REQUEST, groupId = "database", containerFactory = "batchFactory")
    public void saveInDatabase(List<Like> likes) {
        likeManager.saveInDatabase(likes);
    }

    @KafkaListener(topics = TopicConstant.LIKE_REQUEST, groupId = "cache", containerFactory = "batchFactory")
    public void saveInCache(List<Like> likes) {
        List<LikeResult> likeResults = likeManager.saveInCache(likes);
        likeResults.stream()
                .filter(LikeResult::getSuccess)
                .filter(LikeResult::isChange)
                .forEach(result -> {
                    Like like = result.getLike();
                    ResourceTypeEnum type = like.getResourceType();
                    Long resourceId = like.getResourceId();
                    String key = resourceId.toString();
                    String topic = getTopic(type);

                    LikeStateEnum oldState = result.getOldState();
                    LikeStateEnum newState = result.getNewState();
                    if (oldState == LikeStateEnum.DISLIKE && newState == LikeStateEnum.LIKE)
                        producer.asyncSend(topic, key, generateLike(like, LikeStateEnum.CANCEL_DISLIKE));
                    if (oldState == LikeStateEnum.LIKE && newState == LikeStateEnum.DISLIKE)
                        producer.asyncSend(topic, key, generateLike(like, LikeStateEnum.CANCEL_LIKE));

                    producer.asyncSend(topic, key, like);
                });
    }

    private String getTopic(ResourceTypeEnum type) {
        switch (type) {
            case POSTS:
                return TopicConstant.POSTS_LIKE;
            case COMMENT:
                return TopicConstant.COMMENT_LIKE;
            case REPLY:
                return TopicConstant.REPLY_LIKE;
            default:
                return TopicConstant.DEFAULT_LIKE;
        }
    }

    private Like generateLike(Like like, LikeStateEnum state) {
        Like copy = new Like();
        copy.setId(likeManager.generateId());
        copy.setResourceType(like.getResourceType());
        copy.setResourceId(like.getResourceId());
        copy.setState(like.getState());
        copy.setCreateTime(like.getCreateTime());
        copy.setUpdateTime(like.getUpdateTime());
        copy.setCreateBy(like.getCreateBy());
        return copy;
    }

}
