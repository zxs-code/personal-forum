package com.github.code.zxs.resource.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.model.entity.BaseLogicEntity;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_history")
public class History extends BaseLogicEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long resourceId;
    private ResourceTypeEnum resourceType;

    public History(Long createBy, Date createTime, Date updateTime, Long resourceId, ResourceTypeEnum resourceType) {
        super(createBy, createTime, updateTime);
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }
}
