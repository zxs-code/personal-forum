package com.github.code.zxs.core.model.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class CanalBean<T> {
    //数据
    private List<T> data;
    //数据库名称
    private String database;
    private Long es;
    //递增，从1开始
    private Integer id;
    //是否是DDL语句
    private Boolean isDdl;
    //表结构的字段类型
    private Map<String, String> mysqlType;
    //UPDATE语句，旧数据
    private List<T> old;
    //主键名称
    private List<String> pkNames;
    //sql语句
    private String sql;
    private Map<String, Integer> sqlType;
    //表名
    private String table;
    private Long ts;
    //(新增)INSERT、(更新)UPDATE、(删除)DELETE、(删除表)ERASE等等
    private String type;
    //getter、setter方法

    public boolean isDml() {
        return isInsert() || isUpdate() || isDelete();
    }

    public boolean isInsert() {
        return "INSERT".equals(type);
    }

    public boolean isUpdate() {
        return "UPDATE".equals(type);
    }

    public boolean isDelete() {
        return "DELETE".equals(type);
    }

    public boolean isInsertOrUpdate() {
        return isInsert() || isUpdate();
    }
}