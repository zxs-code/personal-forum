package com.github.code.zxs.core.entity;


import com.github.code.zxs.core.enums.ResourceType;
import lombok.Data;

import java.util.Date;

@Data
public class BaseUserEvent<T> {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 资源id
     */
    private Integer resourceId;
    /**
     * 资源类型
     */
    private ResourceType resourceType;
    /**
     * Event实体对象
     */
    private T entity;
    /**
     * 发生时间
     */
    private Date timestamp;
}
