package com.github.code.zxs.user;






@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserDTO extends BaseDTO {
    private String id;
    private String username; //用户名
    private String phone;    //绑定的手机号码
    private String email;    //绑定的邮箱
}
