package com.example.reggie_take_out.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie_take_out.common.BaseContext;
import com.example.reggie_take_out.common.Context;
import com.example.reggie_take_out.entity.ShoppingCart;
import com.example.reggie_take_out.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    private Context<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //设置id
        Long currentId=BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //判断是菜品还是套餐
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId!=null){

            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {

            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //判断是不是已经在数据库里了

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);


        if (cartServiceOne!=null){
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(cartServiceOne);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne=shoppingCart;
        }

        return Context.success(cartServiceOne);
    }

    @GetMapping("/list")
    public Context<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return Context.success(list);
    }

    @DeleteMapping("/clean")
    public Context<String> clean(){

        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return Context.success("清空成功");
    }

    //对购物车进行菜品删除
    @PostMapping("/sub")
    public Context<String> sub(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        Long dishId = shoppingCart.getDishId();

        Long setmealId = shoppingCart.getSetmealId();

        if (dishId!=null){

            queryWrapper.eq(ShoppingCart::getDishId,dishId);

            ShoppingCart one = shoppingCartService.getOne(queryWrapper);

            if (one.getNumber()!=1){

                one.setNumber(one.getNumber()-1);

                shoppingCartService.updateById(one);

            }else{
                shoppingCartService.remove(queryWrapper);
            }
        }else {

            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);

            ShoppingCart one=shoppingCartService.getOne(queryWrapper);

            if (one.getNumber()!=1){
                one.setNumber(one.getNumber()-1);
                shoppingCartService.updateById(one);
            }else {
                shoppingCartService.remove(queryWrapper);
            }
        }

        return Context.success("删除成功" );
    }

}
