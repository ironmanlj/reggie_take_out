package com.example.reggie_take_out.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/*
这个类是为了设置表里面的一些默认字段
 */
public class MyMetaObjectHandler implements MetaObjectHandler {
//    @Autowired
//    private HttpSession httpSession;

    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTIme",LocalDateTime.now());

//        metaObject.setValue("createUser",httpSession.getAttribute("id"));
//        metaObject.setValue("updateUser",httpSession.getAttribute("id"));
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTIme",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
