package com.github.code.zxs.resource.model.bo;

import com.github.code.zxs.resource.model.dto.ResourceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectDetailBO {
    private ResourceDTO resourceDTO;
    private Long collectCount;
    private Long curUserId;
    private Boolean curUserIsCollect;
}
