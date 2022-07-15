package com.github.code.zxs.resource.consumer;

import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.resource.model.bo.UserViewDTO;
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

    //
    @KafkaListener(topics = TopicConstant.USER_VIEW, groupId = "history", containerFactory = "batchFactory")
    public void saveCache(List<UserViewDTO> userViewDTOList) {

        List<History> collect = userViewDTOList.stream().map(e -> new History(
                e.getUserId(),
                e.getResourceDTO().getId(),
                e.getResourceDTO().getType(),
                e.getUserId(),
                e.getTimestamp()))
                .collect(Collectors.toList());
        for (History history : collect) {
            historyService.saveHistory(history);
        }
    }
}
