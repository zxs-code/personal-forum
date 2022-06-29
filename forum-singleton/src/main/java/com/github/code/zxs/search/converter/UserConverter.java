package com.github.code.zxs.search.converter;


import com.github.code.zxs.resource.model.bo.AuthorBO;
import com.github.code.zxs.search.model.bo.UserItemBO;
import com.github.code.zxs.search.model.document.UserDocument;

public class UserConverter {

    private static final UserConverter DEFAULT_CONVERTER = new UserConverter();

    public static UserConverter defaultConverter() {
        return DEFAULT_CONVERTER;
    }

    public UserItemBO toItemBO(UserDocument document) {
        return UserItemBO.builder()
                .id(document.getId())
                .nickname(document.getNickname())
                .avatar(document.getAvatar())
                .signature(document.getSignature())
                .fans(document.getFans())
                .build();
    }

    public AuthorBO toAuthorBO(UserDocument document) {
        return AuthorBO.builder()
                .id(document.getId())
                .nickname(document.getNickname())
                .avatar(document.getAvatar())
                .signature(document.getSignature())
                .build();
    }
}
