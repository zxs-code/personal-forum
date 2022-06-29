package com.github.code.zxs.resource.model.dto;

import com.github.code.zxs.core.model.dto.BaseDTO;
import com.github.code.zxs.resource.model.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends BaseDTO {
    private Long id;
    private String nickname;
    private GenderEnum gender;
    private Date birthday;
    private String signature;
    private String avatar;
    private Integer following;
    private Integer follower;
    private Integer posts;
}
