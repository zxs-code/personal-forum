package com.github.code.zxs.search.model.dto;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.search.model.enums.FilterEnum;
import com.github.code.zxs.search.model.enums.OrderEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SearchDTO {
    private String keyword;
    @NotNull
    private ResourceTypeEnum type;
    private Long curPage;
    private Long pageSize;
    private FilterEnum[] filter;
    private OrderEnum order;
}
