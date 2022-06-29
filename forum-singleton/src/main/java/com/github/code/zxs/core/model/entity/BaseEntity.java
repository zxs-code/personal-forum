package com.github.code.zxs.core.model.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity implements Identity{
    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
//    /**
//     * 修改人id
//     */
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private String updateBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public BaseEntity(Date updateTime) {
        this.updateTime = updateTime;
    }

}
