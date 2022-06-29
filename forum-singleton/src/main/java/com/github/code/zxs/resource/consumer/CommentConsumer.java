package com.github.code.zxs.resource.consumer;

import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.resource.model.entity.Comment;
import com.github.code.zxs.resource.service.manager.CommentManager;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentConsumer {
    @Autowired
    private CommentManager commentManager;

    /**
     * 将评论保存到缓存中
     *
     * @param comments
     * @param consumer
     */
    @KafkaListener(topics = TopicConstant.POSTS_COMMENT, groupId = "cache", containerFactory = "manualBatchFactory")
    public void saveCache(List<Comment> comments, Consumer consumer) {
        commentManager.cacheComment(comments);
        consumer.commitAsync();
//        consumer.commitSync();
    }

    /**
     * 将评论保存到数据库
     *
     *
     */
    @KafkaListener(topics = TopicConstant.POSTS_COMMENT, groupId = "db", containerFactory = "manualBatchFactory")
    public void saveDatabase(List<Comment> comments, Consumer consumer) {
        commentManager.persistComment(comments);
        consumer.commitAsync();
//        consumer.commitSync();
    }
}
