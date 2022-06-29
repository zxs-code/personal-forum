package com.github.code.zxs.resource.consumer;

import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.resource.model.bo.UserEventDTO;
import com.github.code.zxs.resource.model.entity.History;
import com.github.code.zxs.resource.service.biz.base.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistoryConsumer {
    @Autowired
    private HistoryService historyService;

    @KafkaListener(topics = TopicConstant.USER_VIEW, groupId = "history", containerFactory = "batchFactory")
    public void saveCache(List<UserEventDTO> userEventDTOS) {
        List<History> collect = userEventDTOS.stream().map(e -> new History(e.getCreateBy(), e.getCreateTime(),
                e.getUpdateTime(), e.getResourceDTO().getId(), e.getResourceDTO().getType()))
                .collect(Collectors.toList());
        for (History history : collect) {
            historyService.saveHistory(history);
        }
    }
}
