package com.github.code.zxs.user;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.entity.BaseLogicEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

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


    public User(String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
