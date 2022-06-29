package com.github.code.zxs.auth.model.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserLoginDTO {
    @NotNull
    @Pattern(regexp = "^[A-Za-z][A-Za-z\\d]{0,15}$", message = "用户名由数字字母组成，且以字母开头，最大长度16")
    private String username;
    @NotNull
    @Length(min = 6, max = 16, message = "密码长度在6到16位之间")
    private String password;
    @Email
    private String email;

    private Boolean rememberMe = false;
}
