package com.github.code.zxs.resource.model.dto;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class FavAddDTO {
    @NotNull
    private ResourceTypeEnum type;
    @NotNull
    @Length(min = 1, max = 16)
    private String name;
    @Length(max = 128)
    private String cover;
    @Length(max = 128)
    private String description;
}
