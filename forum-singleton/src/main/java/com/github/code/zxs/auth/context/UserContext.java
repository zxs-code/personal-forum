package com.github.code.zxs.auth.context;


import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserContext {
    /**
     * 用户id
     */
    private static final ThreadLocal<Long> idHolder = new ThreadLocal<>();
    /**
     * 用户名
     */
    private static final ThreadLocal<String> usernameHolder = new ThreadLocal<>();
    /**
     * 昵称
     */
    private static final ThreadLocal<String> nicknameHolder = new ThreadLocal<>();
    /**
     * 用户注册时间
     */
    private static final ThreadLocal<Date> registerTimeHolder = new ThreadLocal<>();

    public static Long getId() {
        return idHolder.get();
    }

    public static void setId(Long id) {
        idHolder.set(id);
    }

    public static String getUsername() {
        return usernameHolder.get();
    }

    public static void setUsername(String username) {
        usernameHolder.set(username);
    }

    public static String getNickname(String nickname) {
        return nicknameHolder.get();
    }

    public static void setNickname(String nickname) {
        nicknameHolder.set(nickname);
    }


    public static Date getRegisterTime() {
        return registerTimeHolder.get();
    }

    public static void setRegisterTime(Date createTime) {
        registerTimeHolder.set(createTime);
    }

    public static void clear() {
        idHolder.remove();
        usernameHolder.remove();
        nicknameHolder.remove();
        registerTimeHolder.remove();
    }

}
