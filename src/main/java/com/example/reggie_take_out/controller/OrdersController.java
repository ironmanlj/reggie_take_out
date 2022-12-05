package com.example.reggie_take_out.controller;


import com.example.reggie_take_out.common.Context;
import com.example.reggie_take_out.entity.Orders;
import com.example.reggie_take_out.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public Context<String> sumbit(@RequestBody Orders orders){

        ordersService.submit(orders);

        return Context.success("信息提交成功");

    }
}
