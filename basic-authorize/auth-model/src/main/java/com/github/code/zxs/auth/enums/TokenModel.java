package com.github.code.zxs.auth.enums;

import lombok.Getter;

@Getter
public enum TokenModel {
    UUID("uuid"),
    JWT("jwt");

    TokenModel(String model) {
        this.model = model;
    }

   private String model;
}
