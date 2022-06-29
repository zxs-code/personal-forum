package com.github.code.zxs.search.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserItemBO {
    private Long id;
    private String nickname;
    private String avatar;
    private String signature;
    private Long fans;
}
