package com.example.reggie_take_out.common;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/*
通用的返回结果类，服务端响应的数据最终都会封装成此对象
 */


@Data
public class Context<T> {
    private Integer code;

    private String msg;

    private T data;

    private Map map=new HashMap();

    public static <T> Context<T> success(T object){
        Context<T> context=new Context<T>();
        context.data=object;
        context.code=1;
        return context;
    }

    public static <T> Context<T> error(String msg){
        Context context=new Context();
        context.msg=msg;
        context.code=0;
        return context;
    }

    public Context<T> add(String key, Object value){
        this.map.put(key,value);
        return this;
    }
}
