package com.github.code.zxs.auth.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.auth.model.enums.UserStateEnum;
import lombok.*;

import java.util.Date;

/**
 *
 */
@TableName("tb_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username; //用户名
    private String password; //密码
    private String phone;    //绑定的手机号码
    private String email;    //绑定的邮箱
    private String salt;
    private UserStateEnum state;
    private Date createTime;
    private Date updateTime;


    public User(String username) {
        this.username = username;
    }

}
