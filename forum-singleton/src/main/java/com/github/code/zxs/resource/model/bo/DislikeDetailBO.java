package com.github.code.zxs.resource.model.bo;

import com.github.code.zxs.resource.model.dto.ResourceDTO;
import lombok.Data;

@Data
public class DislikeDetailBO {
    private ResourceDTO resourceDTO;
    private Long collect;
    private Long curUserId;
    private Boolean curUserIsCollect;
}
