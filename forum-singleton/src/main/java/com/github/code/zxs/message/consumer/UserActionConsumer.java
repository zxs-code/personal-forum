package com.github.code.zxs.message.consumer;

import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.message.factory.ResourceFactoryManager;
import com.github.code.zxs.message.manager.RemindManager;
import com.github.code.zxs.message.manager.impl.RemindManagerImpl;
import com.github.code.zxs.message.model.bean.BaseResource;
import com.github.code.zxs.message.model.entity.Remind;
import com.github.code.zxs.message.model.enums.MessageStatus;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.entity.Like;
import com.github.code.zxs.resource.model.enums.ActionEnum;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserActionConsumer {
    @Autowired
    private UserActionConsumer self;
    @Autowired
    private RemindManager remindManager;

    @AllArgsConstructor
    @Getter
    private class MetaData {
        private Long userId;
        private Date remindTime;
    }

    /**
     * 点赞消息提醒
     *
     * @param likes
     */
    @KafkaListener(topics = {TopicConstant.POSTS_LIKE,
            TopicConstant.COMMENT_LIKE,
            TopicConstant.REPLY_LIKE},
            groupId = "message",
            containerFactory = "batchFactory")
    public void sendRemind(List<Like> likes) {
        try {
            RemindManagerImpl.setActionType(ActionEnum.LIKE);
            List<Like> list = likes.stream().filter(like -> like.getState() == LikeStateEnum.LIKE).collect(Collectors.toList());
            Map<ResourceDTO, Long> countMap = list.stream().collect(Collectors.groupingBy(like -> new ResourceDTO(like.getResourceId(), like.getResourceType()), Collectors.counting()));
            Map<ResourceDTO, MetaData> metaDataMap = list.stream().collect(Collectors.toMap(like -> new ResourceDTO(like.getResourceId(), like.getResourceType()), like -> new MetaData(like.getCreateBy(), like.getUpdateTime()), (o, n) -> n));

            for (ResourceDTO resourceDTO : countMap.keySet()) {
                MetaData metaData = metaDataMap.get(resourceDTO);
                long sender = metaData.getUserId();
                Date remindTime = metaData.getRemindTime();
                long counts = countMap.get(resourceDTO);
                long resourceId = resourceDTO.getId();
                ResourceTypeEnum resourceType = resourceDTO.getType();
                Remind remind = remindManager.getRemind(resourceDTO, ActionEnum.LIKE, Remind::getId, Remind::getCounts, Remind::getStatus);
                if (remind != null) {
                    if (remind.getStatus() == MessageStatus.IGNORE)
                        continue;
                    remind.setSender(sender);
                    remind.setCounts(counts);
                    remind.setRemindTime(remindTime);
                    remindManager.update(remind);
                    continue;
                }
                BaseResource resource = ResourceFactoryManager.getFactory(resourceDTO.getType()).getResource();
                remind = Remind.builder()
                        .sender(sender)
                        .title(resource.getTitle())
                        .receiver(resource.getAuthor())
                        .resourceId(resourceId)
                        .resourceType(resourceType)
                        .action(ActionEnum.LIKE)
                        .counts(1L)
                        .remindTime(remindTime)
                        .status(MessageStatus.UNREAD)
                        .build();
                remindManager.insert(remind);
            }
        } finally {
            RemindManagerImpl.removeActionType();
        }
    }


}
