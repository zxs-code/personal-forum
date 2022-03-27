package com.github.code.zxs.resource.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.entity.BaseLogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收藏夹
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_favorites")
public class Favorites extends BaseLogicEntity {
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 封面url
     */
    private String cover;
}
