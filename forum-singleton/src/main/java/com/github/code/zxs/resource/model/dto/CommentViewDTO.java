package com.github.code.zxs.resource.model.dto;

import com.github.code.zxs.resource.model.enums.CommentOrderEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
public class CommentViewDTO {
    @NonNull
    private ResourceDTO resource;
    private CommentOrderEnum orderBy = CommentOrderEnum.HOT;
    private Long next = 0L;
    @Null
    private Long count;

    public Long getNext() {
        return next >= 0 ? next : 0;
    }
}
