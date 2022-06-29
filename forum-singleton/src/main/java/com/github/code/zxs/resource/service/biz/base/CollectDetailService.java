package com.github.code.zxs.resource.service.biz.base;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.code.zxs.resource.model.entity.CollectDetail;

import java.util.Date;
import java.util.List;

public interface CollectDetailService extends IService<CollectDetail> {

    List<CollectDetail> listCollectState(Long collectId);

    int reverseState(List<Long> updateFavIds, Long collectId, Date updateTime);
}
