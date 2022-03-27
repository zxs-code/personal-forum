package com.github.code.zxs.core.auth.dto;










@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfoDTO {
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 用户登录时间
     */
    private Date loginTime;
    /**
     * 登录失效时间，null则不会主动失效
     */
    private Date expiration;
    /**
     * token保存的用户信息
     */
    private TokenUserDTO userDTO;

    public List<String>  getToken(){
        List<String> tokens = new ArrayList<>(2);
        tokens.add(accessToken);
        tokens.add(refreshToken);
        return tokens;
    }
}
