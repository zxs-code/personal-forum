package com.github.code.zxs.resource.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorBO {
    private Long id;
    private String nickname;
    private String avatar;
    private String signature;
    public static final AuthorBO ANONYMOUS_AUTHOR = new AuthorBO(-1L,"匿名用户");
    public static final AuthorBO DEACTIVATE_AUTHOR = new AuthorBO(-2L,"已注销用户");

    public AuthorBO(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
