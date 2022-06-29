package com.github.code.zxs.resource.model.dto;

import com.github.code.zxs.core.model.dto.BaseDTO;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;

public class UserEventMessageDTO extends BaseDTO {
    private ResourceTypeEnum resourceType;
    private Long resourceId;
    private Boolean state;
}
