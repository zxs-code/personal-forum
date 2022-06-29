package com.github.code.zxs.auth.model.bo;

import com.github.code.zxs.auth.model.enums.UserStateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBO {
    private Long id;
    private String username; //用户名
    private String phone;    //绑定的手机号码
    private String email;    //绑定的邮箱
    private UserStateEnum state;
    private Date createTime;
    private Date updateTime;
}
