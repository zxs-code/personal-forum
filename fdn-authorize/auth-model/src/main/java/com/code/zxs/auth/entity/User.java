package com.code.zxs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("tb_user")
@Data
public class User {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String username; //用户名
    private String password; //密码
    private String phone;    //绑定的手机号码
    private String email;    //绑定的邮箱
    private Date createTime;
    private Date updateTime;
}
