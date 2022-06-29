package com.github.code.zxs.resource.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostsAddDTO extends BasePostsDTO {
    private Boolean anonymous;
}
