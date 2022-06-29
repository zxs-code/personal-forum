package com.github.code.zxs.resource.model.dto;

import com.github.code.zxs.core.model.dto.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserEventDTO extends BaseDTO {
    private Boolean state;
    private ResourceDTO resource;

    public UserEventDTO(ResourceDTO resource, Boolean state) {
        this.resource = resource;
        this.state = state;
    }
}
