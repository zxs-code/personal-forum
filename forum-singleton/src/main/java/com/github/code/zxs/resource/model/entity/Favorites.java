package com.github.code.zxs.resource.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.model.entity.BaseLogicEntity;
import lombok.*;

/**
 * 收藏夹
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_favorites")
public class Favorites extends BaseLogicEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 收藏夹描述
     */
    private String description;
    /**
     * 封面url
     */
    private String cover;
    /**
     * 收藏总数
     */
    private Integer total;
    /**
     * 收藏上限
     */
    private Integer maxTotal;
}
