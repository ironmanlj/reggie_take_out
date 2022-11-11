package com.example.reggie_take_out.common;
//全局异常处理


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;


@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GLobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Context<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split=ex.getMessage().split(" ");
            String message=split[2]+"已存在";
            return Context.error(message);
        }

        return Context.error("未知错误");
    }
}
