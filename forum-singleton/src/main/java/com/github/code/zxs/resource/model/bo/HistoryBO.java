package com.github.code.zxs.resource.model.bo;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import lombok.Data;

@Data
public class HistoryBO {
    private Long id;
    private ResourceTypeEnum resourceType;
    private Long resourceId;
    private String title;
}
