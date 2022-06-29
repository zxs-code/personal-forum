package com.github.code.zxs.core.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class BaseDTO {
    /**
     * 创建人id
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;


    public BaseDTO(Long createBy, Date createTime) {
        this.createBy = createBy;
        this.createTime = createTime;
    }

    public BaseDTO(Long createBy) {
        this.createBy = createBy;
    }
}
