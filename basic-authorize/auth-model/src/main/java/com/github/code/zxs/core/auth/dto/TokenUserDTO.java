package com.github.code.zxs.core.auth.dto;











@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenUserDTO {
    /**
     * 用户id
     */
    private Object id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 注册时间
     */
    private Date createTime;
    /**
     * 用户拥有的角色
     */
    private Set<BaseRole> roles;
    /**
     * 额外信息
     */
    private Map<Object, Object> extra;

    public TokenUserDTO(String id) {
        this.id = id;
    }

    public TokenUserDTO(String id, String username, Date createTime) {
        this.id = id;
        this.username = username;
        this.createTime = createTime;
    }

}
