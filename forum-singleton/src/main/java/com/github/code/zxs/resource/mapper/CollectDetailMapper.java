package com.github.code.zxs.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.code.zxs.resource.model.entity.CollectDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CollectDetailMapper extends BaseMapper<CollectDetail> {
    /**
     * 反转收藏状态
     *
     * @param updateFavIds
     * @param collectId
     */
    int reverseState(
            @Param("update_fav_ids") List<Long> updateFavIds,
            @Param("collect_id") Long collectId,
            @Param("update_time") Date updateTime
    );
}
