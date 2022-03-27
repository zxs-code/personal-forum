package com.github.code.zxs.core.auth.annotation;





/**
 * 持有满足条件角色才能访问
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface HasRole {
    /**
     * 角色名
     *
     * @return
     */
    String[] name() default {};

    /**
     * 判断策略
     * @return
     */
    RolesStrategy strategy() default RolesStrategy.ANY;
}
