package com.github.code.zxs.resource.consumer;

import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.resource.model.entity.Reply;
import com.github.code.zxs.resource.service.manager.ReplyManager;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReplyConsumer {
    @Autowired
    private ReplyManager replyManager;

    /**
     * 将评论保存到缓存中
     *
     * @param replies
     * @param consumer
     */
    @KafkaListener(topics = TopicConstant.POSTS_REPLY, groupId = "cache", containerFactory = "manualBatchFactory")
    public void saveCache(List<Reply> replies, Consumer consumer) {
        replyManager.cacheReply(replies);
        consumer.commitAsync();
//        consumer.commitSync();
    }

    /**
     * 将评论保存到数据库
     */
    @KafkaListener(topics = TopicConstant.POSTS_REPLY, groupId = "db", containerFactory = "manualBatchFactory")
    public void saveDatabase(List<Reply> replies, Consumer consumer) {
        replyManager.persistReply(replies);
        consumer.commitAsync();
//        consumer.commitSync();
    }
}
