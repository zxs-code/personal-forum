package com.github.code.zxs.core.entity;


import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 逻辑删除
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseLogicEntity extends BaseEntity{
    /**
     * 删除时间
     */
    @TableLogic(value = "NULL", delval = "NOW()")
    private Date deleteTime;
}
