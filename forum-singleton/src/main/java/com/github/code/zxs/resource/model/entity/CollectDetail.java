package com.github.code.zxs.resource.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 收藏详情
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_collect_detail")
public class CollectDetail extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long collectId;
    private Long favId;
    private Boolean state;

    public CollectDetail(Long createBy, Date createTime, Date updateTime, Long collectId, Long favId, Boolean state) {
        super(createBy, createTime, updateTime);
        this.collectId = collectId;
        this.favId = favId;
        this.state = state;
    }

    public CollectDetail(Long collectId, Long favId, Boolean state) {
        this.collectId = collectId;
        this.favId = favId;
        this.state = state;
    }
}
