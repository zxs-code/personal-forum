package com.github.code.zxs.core.user.service;

public interface UserService {
    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     */
    void register(String username, String password);

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     */
    void login(String username, String password);

    /**
     * 获取用户登录信息
     * @param userId 用户id
     */
    void getLoginInfo(String userId);

    /**
     * 修改密码
     * @param userId
     * @param password
     */
    void modifyPassword(String userId,String password);

    /**
     * 绑定邮箱
     * @param userId
     * @param email
     */
    void bindEmail(String userId,String email);

    /**
     * 绑定手机号码
     * @param userId
     * @param phone
     */
    void bindPhone(String userId,String phone);

    /**
     * 邮箱是否可用
     * @param email
     * @return
     */
    Boolean emailAvailable(String email);

    /**
     * 手机号码是否可用
     * @param phone
     * @return
     */
    Boolean phoneAvailable(String phone);
}
