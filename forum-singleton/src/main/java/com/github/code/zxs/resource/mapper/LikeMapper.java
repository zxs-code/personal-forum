package com.github.code.zxs.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.code.zxs.resource.model.entity.Like;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeMapper extends BaseMapper<Like> {
    /**
     * 批量保存或更新 mysql on duplicate key
     *
     * @param likes
     * @return
     */
    Long insertIgnore(List<Like> likes);

    void updateBatch(List<Like> likes);

    Long saveOrUpdate(List<Like> likes);
}
