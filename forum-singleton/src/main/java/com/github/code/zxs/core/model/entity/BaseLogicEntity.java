package com.github.code.zxs.core.model.entity;


import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 逻辑删除
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public abstract class BaseLogicEntity extends BaseEntity{
    /**
     * 删除时间
     */
    @TableLogic(value = "NULL", delval = "NOW()")
    private Date deleteTime;

    public BaseLogicEntity(Long createBy, Date createTime, Date updateTime) {
        super(createBy, createTime, updateTime);
    }
}
