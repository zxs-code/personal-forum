package com.github.code.zxs.resource.service.biz.base;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.code.zxs.resource.model.dto.CollectDTO;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.entity.Collect;

import java.util.Set;

public interface CollectService extends IService<Collect> {
    void collect(CollectDTO collectDTO);

    long countResourceCollect(ResourceDTO resourceDTO);

    boolean userIsCollect(Long userId, ResourceDTO resourceDTO);

    Set<Long> userSelectedFavIds(Long userId, ResourceDTO resourceDTO);
}
