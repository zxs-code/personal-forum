package com.github.code.zxs.core.generator.base;


import org.aspectj.lang.Signature;

/**
 * 速率限制key构造器接口
 */
public interface KeyGenerator {

    /**
     * @param c         目标对象Class
     * @param signature 方法签名
     * @param args      方法参数
     * @return
     */
    String get(Class c, Signature signature, Object[] args);
}
