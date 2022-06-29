package com.github.code.zxs.resource.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserInfoBO {
    private Long id;
    private String nickname;
    private String avatar;
    private Date expireAt;
}
