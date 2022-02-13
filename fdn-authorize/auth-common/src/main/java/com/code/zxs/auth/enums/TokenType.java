package com.code.zxs.auth.enums;

public   enum  TokenType {
    ACCESS_TOKEN("access_token","访问令牌"),
    REFRESH_TOKEN("refresh_token","刷新令牌");
    private String type;
    private String description;

    TokenType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
