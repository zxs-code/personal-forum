package com.github.code.zxs.resource.model.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 分区（版块）
 */

@Data
public class PartitionDTO {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 版块名
     */
    private String name;
    /**
     * 版块描述
     */
    private String description;
}
