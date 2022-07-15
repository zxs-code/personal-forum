package com.github.code.zxs.resource.model.bo;

import com.github.code.zxs.auth.model.enums.UserStateEnum;
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
public class UserInfoBO {
    private Long id;
    private String nickname;
    private String avatar;
    private String signature;
    private GenderEnum gender;
    private Date birthday;

    private String username;
    private String phone;
    private String email;
    private UserStateEnum state;
    private Date createTime;
    private Date updateTime;
}
