package com.github.code.zxs.resource.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.entity.BaseLogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_tag")
public class Tag extends BaseLogicEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer groupId = 0;
    private String name;
}
