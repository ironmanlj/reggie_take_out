package com.example.reggie_take_out.common;

/**
 * @创建人 ironman_lj
 * @创建时间 2022-11-21
 * @描述 自定义业务异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
