package com.github.code.zxs.core.component;

/**
 * 用户信息提供者
 */
public interface UserProvider {
    /**
     * 数据库插入更新数据填充返回的用户标识
     * @return
     */
    Object getUserIdentify();
}
