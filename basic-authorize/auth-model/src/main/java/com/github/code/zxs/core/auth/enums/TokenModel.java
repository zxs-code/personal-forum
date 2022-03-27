package com.github.code.zxs.core.auth.enums;



@Getter
public enum TokenModel {
    UUID("uuid"),
    JWT("jwt");

    TokenModel(String model) {
        this.model = model;
    }

   private String model;
}
