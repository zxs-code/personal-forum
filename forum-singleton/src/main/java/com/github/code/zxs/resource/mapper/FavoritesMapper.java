package com.github.code.zxs.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.resource.model.entity.Favorites;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FavoritesMapper extends BaseMapper<Favorites> {

    @Select({"SELECT id,name,total,max_total",
            "FROM tb_favorites",
            "WHERE create_by = #{userId} AND",
            "resource_type = #{resourceType} AND",
            "resource_id = #{resourceId}"})
    List<Favorites> listFav(@Param("userId") Long userId, @Param("resourceType") ResourceTypeEnum resourceType, @Param("resourceId") Long resourceId);


    int incrFavTotal(@Param("fav_ids") Set<Long> favIds);

    int decrFavTotal(@Param("fav_ids") Set<Long> favIds);
}
