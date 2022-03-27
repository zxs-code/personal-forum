package com.github.code.zxs.core.dto;





@Data
public class BaseDTO {
    /**
     * 创建人id
     */
    private String createBy;
    /**
     * 修改人id
     */
    private String updateBy;
    /**
     * 删除人id
     */
    private String deleteBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 删除时间
     */
    private Date deleteTime;
}
