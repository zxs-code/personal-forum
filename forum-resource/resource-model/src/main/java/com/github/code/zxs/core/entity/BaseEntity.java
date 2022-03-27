package com.github.code.zxs.core.entity;


import lombok.Data;

import java.util.Date;


@Data
public class BaseEntity {
    /**
     * 创建人id
     */
    private String createBy;
    /**
     * 修改人id
     */
    private String updateBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}
