package com.github.code.zxs.auth.dto;

import com.github.code.zxs.auth.enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
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
}
