package com.github.code.zxs.core.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MybatisPlusConfig {

    private static final ThreadLocal<Map<String, String>> DYNAMIC_TABLE_NAME_MAP = new ThreadLocal<>();

    public static void setDynamicTableName(String sourceName, String newName) {
        tableNameMap().put(sourceName, newName);
    }

    public static String getDynamicTableName(String sourceName) {
        return tableNameMap().get(sourceName);
    }

    public static String getDynamicTableName(String sourceName, String defaultValue) {
        return tableNameMap().getOrDefault(sourceName, defaultValue);
    }

    public static void removeDynamicTableName(String sourceName) {
        tableNameMap().remove(sourceName);
    }

    public static void clearDynamicTableName() {
        tableNameMap().clear();
    }

    private static Map<String, String> tableNameMap() {
        if (DYNAMIC_TABLE_NAME_MAP.get() == null)
            DYNAMIC_TABLE_NAME_MAP.set(new HashMap<>());
        return DYNAMIC_TABLE_NAME_MAP.get();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        //替换动态表名
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> getDynamicTableName(tableName, tableName));
        //清空动态表名
//        dynamicTableNameInnerInterceptor.setHook(MybatisPlusConfig::clearDynamicTableName);
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        return interceptor;
    }
}