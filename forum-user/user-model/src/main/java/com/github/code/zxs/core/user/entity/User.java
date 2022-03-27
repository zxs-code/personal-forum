package com.github.code.zxs.core.user.entity;










/**
 *
 */

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("tb_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseLogicEntity {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String username; //用户名
    private String password; //密码
    private String phone;    //绑定的手机号码
    private String email;    //绑定的邮箱


    public UserDTO toUserDTO(){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(this,userDTO);
        return userDTO;
    }

    public TokenUserDTO toTokenUserDTO(){
        TokenUserDTO tokenUserDTO = new TokenUserDTO();
        BeanUtils.copyProperties(this,tokenUserDTO);
        return tokenUserDTO;
    }

    public User(String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
