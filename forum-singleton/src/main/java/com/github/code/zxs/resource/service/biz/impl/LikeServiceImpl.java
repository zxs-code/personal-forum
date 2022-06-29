package com.github.code.zxs.resource.service.biz.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.core.exception.UserEventException;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.support.message.MessageProducer;
import com.github.code.zxs.resource.converter.LikeConverter;
import com.github.code.zxs.resource.mapper.LikeMapper;
import com.github.code.zxs.resource.model.dto.LikeDTO;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.entity.Like;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import com.github.code.zxs.resource.service.biz.base.LikeService;
import com.github.code.zxs.resource.service.manager.LikeManager;
import com.github.code.zxs.resource.support.generator.DistributedIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {
    @Autowired
    private DistributedIdGenerator idGenerator;
    @Autowired
    private LikeConverter likeConverter;
    @Autowired
    private LikeManager likeManager;
    @Autowired
    private MessageProducer producer;

    @Override
    public void asyncLike(LikeDTO likeDTO) {
        ResourceDTO resource = likeDTO.getResource();
        if (resource == null || likeDTO.getState() == null)
            return;
        checkType(resource.getType());
        Like likeEntity = likeConverter.dto2Entity(likeDTO);
        Date now = new Date();
        long userId = UserContext.getId();
        likeEntity.setId(likeManager.generateId());
        likeEntity.setCreateTime(now);
        likeEntity.setUpdateTime(now);
        likeEntity.setCreateBy(userId);
        producer.asyncSend(TopicConstant.LIKE_REQUEST, resource.getId().toString(), likeEntity);
    }

    @Override
    public Long count(ResourceDTO resourceDTO, LikeStateEnum stateEnum) {
        return likeManager.count(resourceDTO, stateEnum);
    }

    @Override
    public List<Long> count(List<ResourceDTO> resourceDTO, LikeStateEnum stateEnum) {
        return likeManager.count(resourceDTO, stateEnum);
    }

    @Override
    public LikeStateEnum userLikeState(Long userId, ResourceDTO resourceDTO) {
        return likeManager.userLikeState(userId, resourceDTO);
    }

    @Override
    public List<LikeStateEnum> userLikeState(Long userId, ResourceTypeEnum resourceType, List<Long> resourceIds) {
        return likeManager.userLikeState(userId, resourceType, resourceIds);
    }

    @Override
    public PageResult<Long> getRecentLikeResourceId(Long userId, ResourceTypeEnum type, PageDTO pageDTO) {
        return likeManager.getRecentLikeResourceId(userId, type, pageDTO);
    }

    private void checkType(ResourceTypeEnum type) {
        switch (type) {
            case POSTS:
            case COMMENT:
            case REPLY:
                return;
            default:
                throw new UserEventException("不允许的资源类型", UserContext.getId());
        }
    }
}
