package com.github.code.zxs.resource.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyViewDTO {
    @NotNull
    private Long commentId;
    private Long next = 0L;
    @Null
    private Long count;

    public Long getNext() {
        return next >= 0 ? next : 0;
    }
}
