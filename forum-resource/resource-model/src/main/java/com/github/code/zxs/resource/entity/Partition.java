package com.github.code.zxs.resource.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.entity.BaseLogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分区（版块）
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_partition")
public class Partition extends BaseLogicEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 父级版块id
     */
    private Integer parentId = 0;
    /**
     * 版块分组id
     */
    private Integer groupId = 0;
    /**
     * 版块名
     */
    private String name;
    /**
     * 版块描述
     */
    private String description;
}
