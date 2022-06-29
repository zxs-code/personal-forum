package com.github.code.zxs.resource.converter;

import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.resource.model.dto.LikeDTO;
import com.github.code.zxs.resource.model.dto.UserEventDTO;
import com.github.code.zxs.resource.model.dto.UserEventMessageDTO;
import com.github.code.zxs.resource.model.entity.Like;
import com.github.code.zxs.resource.support.generator.DistributedIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LikeConverter {

    public void event2Message(UserEventDTO eventDTO, UserEventMessageDTO messageDTO) {
        BeanUtils.copy(eventDTO, messageDTO);
    }

    public Like dto2Entity(LikeDTO likeDTO) {
        Like like = new Like();
        like.setResourceId(likeDTO.getResource().getId());
        like.setResourceType(likeDTO.getResource().getType());
        like.setState(likeDTO.getState());
        return like;
    }
}
