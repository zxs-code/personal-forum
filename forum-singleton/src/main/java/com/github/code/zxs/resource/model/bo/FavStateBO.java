package com.github.code.zxs.resource.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavStateBO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 收藏总数
     */
    private Integer total;
    /**
     * 收藏上限
     */
    private Integer maxTotal;

    private Boolean state;
}
