package com.github.code.zxs.resource.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class DialogViewDTO {
    @NotNull
    private Long commentId;
    @NotNull
    private Long dialogId;
    @NotNull
    private Long next;
    @Null
    private Long count;
}
