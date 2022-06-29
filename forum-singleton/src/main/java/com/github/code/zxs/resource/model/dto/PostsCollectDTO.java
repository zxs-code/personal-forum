package com.github.code.zxs.resource.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PostsCollectDTO {
    private Long id;
    private Set<Long> addFavIds;
    private Set<Long> delFavIds;
}
