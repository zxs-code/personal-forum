package com.github.code.zxs.resource.service.biz.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.resource.mapper.CollectDetailMapper;
import com.github.code.zxs.resource.model.entity.CollectDetail;
import com.github.code.zxs.resource.service.biz.base.CollectDetailService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CollectDetailServiceImpl extends ServiceImpl<CollectDetailMapper, CollectDetail> implements CollectDetailService {

    /**
     * 获取某条收藏记录收藏夹状态
     * @param collectId
     * @return
     */
    public List<CollectDetail> listCollectState(Long collectId) {
        return baseMapper.selectList(new LambdaQueryWrapper<CollectDetail>()
                .select(CollectDetail::getFavId, CollectDetail::getState)
                .eq(CollectDetail::getCollectId,collectId));
    }

    @Override
    public int reverseState(List<Long> updateFavIds, Long collectId, Date updateTime) {
        return baseMapper.reverseState(updateFavIds, collectId, updateTime);
    }
}
