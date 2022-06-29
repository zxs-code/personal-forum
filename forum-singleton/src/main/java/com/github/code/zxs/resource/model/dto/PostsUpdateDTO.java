package com.github.code.zxs.resource.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostsUpdateDTO extends BasePostsDTO {
    @NotNull
    private Long id;
}
