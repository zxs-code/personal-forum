package com.github.code.zxs.core.auth.enums;



@Getter
public enum TokenType {
    ACCESS_TOKEN("access_token","访问令牌"),
    REFRESH_TOKEN("refresh_token","刷新令牌");
    private String type;
    private String description;

    TokenType(String type, String description) {
        this.type = type;
        this.description = description;
    }

}
