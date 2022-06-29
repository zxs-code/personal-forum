package com.github.code.zxs.resource.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.model.entity.BaseEntity;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("tb_like")
@NoArgsConstructor
@AllArgsConstructor
public class Like extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private ResourceTypeEnum resourceType;
    private Long resourceId;
    private LikeStateEnum state;

    public Like(ResourceTypeEnum resourceType, Long resourceId, LikeStateEnum state) {
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.state = state;
    }
}
