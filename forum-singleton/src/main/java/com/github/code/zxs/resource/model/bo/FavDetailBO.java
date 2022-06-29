package com.github.code.zxs.resource.model.bo;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavDetailBO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 封面
     */
    private String cover;
    /**
     * 描述
     */
    private String description;
    /**
     * 收藏夹类型
     */
    private ResourceTypeEnum type;
    /**
     * 收藏总数
     */
    private Integer total;
    /**
     * 收藏上限
     */
    private Integer maxTotal;
    /**
     * 创建时间
     */
    private Date createTime;

    private Date updateTime;

    private AuthorBO author;
}
