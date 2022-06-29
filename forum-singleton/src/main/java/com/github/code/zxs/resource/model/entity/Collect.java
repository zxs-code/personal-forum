package com.github.code.zxs.resource.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_collect")
public class Collect extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long resourceId;
    private Boolean state;


    public Collect(Long resourceId, Boolean state) {
        this.resourceId = resourceId;
        this.state = state;
    }

}
