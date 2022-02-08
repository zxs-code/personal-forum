package com.code.zxs.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        metaObject.setValue("createTime",now);
        metaObject.setValue("updateTime",now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date now = new Date();
        metaObject.setValue("updateTime",now);
    }
}
