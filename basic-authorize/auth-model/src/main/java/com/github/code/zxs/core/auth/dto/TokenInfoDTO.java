package com.github.code.zxs.core.auth.dto;









@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfoDTO {
    private Date issueTime;
    /**
     * token过期时间
     */
    private Date expiration;
    /**
     * token保存的用户信息
     */
    private TokenUserDTO userDTO;
    /**
     * token类型
     */
    private TokenType type;

    private String token;

    public TokenInfoDTO(TokenUserDTO userDTO, String token) {
        this.userDTO = userDTO;
        this.token = token;
    }
}
