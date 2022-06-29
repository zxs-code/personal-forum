package com.github.code.zxs.resource.service.biz.base;

import com.github.code.zxs.core.component.PageDTO;
import com.github.code.zxs.core.component.PageResult;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.resource.model.dto.LikeDTO;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;

import java.util.List;

public interface LikeService {

    void asyncLike(LikeDTO likeDTO);

    Long count(ResourceDTO resourceDTO, LikeStateEnum stateEnum);

    List<Long> count(List<ResourceDTO> resourceDTO, LikeStateEnum stateEnum);

    LikeStateEnum userLikeState(Long userId, ResourceDTO resourceDTO);

    List<LikeStateEnum> userLikeState(Long userId, ResourceTypeEnum resourceType, List<Long> resourceIds);

    PageResult<Long> getRecentLikeResourceId(Long userId, ResourceTypeEnum type, PageDTO pageDTO);

}
