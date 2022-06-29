package com.github.code.zxs.resource.model.dto;

import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LikeDTO {
    @NotNull
    private ResourceDTO resource;
    @NotNull
    private LikeStateEnum state;
}
