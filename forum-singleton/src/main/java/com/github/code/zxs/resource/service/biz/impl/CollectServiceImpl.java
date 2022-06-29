package com.github.code.zxs.resource.service.biz.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.core.annotation.Lock;
import com.github.code.zxs.core.constant.TopicConstant;
import com.github.code.zxs.core.exception.UserEventException;
import com.github.code.zxs.core.model.entity.BaseEntity;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.core.support.message.MessageProducer;
import com.github.code.zxs.core.util.CollectionUtils;
import com.github.code.zxs.core.util.RedisUtil;
import com.github.code.zxs.resource.mapper.CollectMapper;
import com.github.code.zxs.resource.mapper.FavoritesMapper;
import com.github.code.zxs.resource.model.dto.CollectDTO;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.entity.Collect;
import com.github.code.zxs.resource.model.entity.CollectDetail;
import com.github.code.zxs.resource.model.entity.Favorites;
import com.github.code.zxs.resource.service.biz.base.CollectDetailService;
import com.github.code.zxs.resource.service.biz.base.CollectService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private CollectServiceImpl collectService;
    @Autowired
    private CollectDetailService collectDetailService;
    @Autowired
    private MessageProducer producer;
    @Autowired
    private FavoritesMapper favoritesMapper;


    @Override
    @Lock(prefix = "collect", key = "#collectDTO.createBy")
    @Transactional(rollbackFor = Exception.class)
    public void collect(CollectDTO collectDTO) {
        //用户所有收藏夹id
        long userId = collectDTO.getCreateBy();
        ResourceDTO resourceDTO = collectDTO.getResourceDTO();

        Collect collect = collectService.getCollect(userId, resourceDTO);
        if (collect == null) {
            collect = new Collect(resourceDTO.getId(), true);
            baseMapper.insert(collect);
            //id回显
        }
        long collectId = collect.getId();

        Set<Long> favIds = listFavId(userId, collectDTO.getResourceDTO().getType());
        List<CollectDetail> collectDetails = collectDetailService.listCollectState(collectId);
        //将收藏夹id按state分类
        Map<Boolean, Set<Long>> favState = collectDetails.stream().collect(Collectors.partitioningBy(CollectDetail::getState, Collectors.mapping(CollectDetail::getFavId, Collectors.toSet())));

        Set<Long> addFavIds = collectDTO.getAddFavIds();
        Set<Long> delFavIds = collectDTO.getDelFavIds();


        addFavIds.retainAll(favIds);
        addFavIds.removeAll(favState.get(true));

        delFavIds.retainAll(favState.get(true));

        //验证是否达到收藏上限
        if (CollectionUtils.isNotEmpty(addFavIds) &&
                favoritesMapper.incrFavTotal(addFavIds) != collectDTO.getAddFavIds().size())
            throw new UserEventException("收藏夹数量达到上限", collectDTO.getCreateBy());
        if (CollectionUtils.isNotEmpty(delFavIds))
            favoritesMapper.decrFavTotal(delFavIds);

        List<CollectDetail> insertCollectDetatil = new ArrayList<>();
        List<Long> updateFavIds = new ArrayList<>();

        for (Long favId : addFavIds) {
            if (favState.get(false).contains(favId))
                updateFavIds.add(favId);
            else
                insertCollectDetatil.add(new CollectDetail(collectId, favId, true));
        }
        updateFavIds.addAll(delFavIds);

        collectDetailService.saveBatch(insertCollectDetatil);
        if (CollectionUtils.isNotEmpty(updateFavIds))
            collectDetailService.reverseState(updateFavIds, collectId, collectDTO.getUpdateTime());
        Boolean newState = addFavIds.size() + favState.get(true).size() - delFavIds.size() > 0;
        if (!newState.equals(collect.getState())) {
            baseMapper.updateById(new Collect(collectId, newState));
            producer.asyncSend(TopicConstant.POSTS_COLLECT, collect);
        }
    }

    @Override
    public long countResourceCollect(ResourceDTO resourceDTO) {
        if (resourceDTO.getType() == ResourceTypeEnum.POSTS) {
            return baseMapper.selectCount(new LambdaQueryWrapper<Collect>()
                    .eq(Collect::getResourceId, resourceDTO.getId())
                    .eq(Collect::getState, true));
        }
        return 0;
    }

    @Override
    public boolean userIsCollect(Long userId, ResourceDTO resourceDTO) {
        if (resourceDTO.getType() == ResourceTypeEnum.POSTS) {
            return baseMapper.exists(new LambdaQueryWrapper<Collect>()
                    .eq(Collect::getResourceId, resourceDTO.getId())
                    .eq(Collect::getCreateBy, userId)
                    .eq(Collect::getState, true));
        }
        return false;
    }

    @Override
    public Set<Long> userSelectedFavIds(Long userId, ResourceDTO resourceDTO) {
        Collect collect = collectService.getCollect(userId, resourceDTO);
        if (collect == null)
            return Collections.emptySet();
        return collectDetailService.listCollectState(collect.getId())
                .stream()
                .filter(CollectDetail::getState)
                .map(CollectDetail::getFavId)
                .collect(Collectors.toSet());
    }

    public Set<Long> listFavId(Long userId, ResourceTypeEnum type) {
        if (type == ResourceTypeEnum.POSTS)
            return CollectionUtils.castToSet(favoritesMapper.selectObjs(new LambdaQueryWrapper<Favorites>()
                    .select(Favorites::getId)
                    .eq(BaseEntity::getCreateBy, userId)), Long.class);
        return null;
    }

    public Collect getCollect(Long userId, ResourceDTO resourceDTO) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Collect>()
                .eq(Collect::getResourceId, resourceDTO.getId())
                .eq(Collect::getCreateBy, userId)
                .eq(BaseEntity::getCreateBy, userId));
    }
}
