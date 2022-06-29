package com.github.code.zxs.resource.model.dto;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentAddDTO {
    @NotNull
    private Long resourceId;
    @NotNull
    private ResourceTypeEnum resourceType;
    @NotNull
    private String content;
}
