package com.github.code.zxs.core.user.dto;













@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfoDTO {
    private String id;
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
    /**
     * 用户登录时的ip地址
     */
    private String ip;
    /**
     * 用户登录时的设备类型
     */
    private Device device;
    /**
     * 用户代理
     */
    private String userAgent;
    /**
     * 是否可以强制下线
     */
    private Boolean canRemove = false;

    /**
     * 移除token时使用
     *
     * @return
     */
    public List<TokenInfoDTO> toTokenInfoDTO() {
        List<TokenInfoDTO> list = new ArrayList<>();
        list.add(new TokenInfoDTO(userDTO, accessToken));
        list.add(new TokenInfoDTO(userDTO, refreshToken));
        return list;
    }
}
