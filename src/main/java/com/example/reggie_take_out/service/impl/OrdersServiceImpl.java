package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.common.BaseContext;
import com.example.reggie_take_out.common.CustomException;
import com.example.reggie_take_out.entity.*;
import com.example.reggie_take_out.mapper.OrdersMapper;
import com.example.reggie_take_out.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    @Transactional
    public void submit(Orders orders) {
        //获取当前用户
        Long userId = BaseContext.getCurrentId();
        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,userId);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        if (list==null||list.size()==0){
            throw new CustomException("业务异常，购物车为空");
        }
        //查询用户数据
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();

        AddressBook addressBook = addressBookService.getById(addressBookId);

        long orderId = IdWorker.getId();

        AtomicInteger amount = new AtomicInteger(0);


        //计算总金额


        List<OrderDetail> orderDetails=list.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        //向订单表插入数据

        orders.setOrderTime(LocalDateTime.now());

        orders.setNumber(String.valueOf(orderId));

        orders.setCheckoutTime(LocalDateTime.now());

        orders.setStatus(2);

        orders.setUserId(userId);

        orders.setAmount(new BigDecimal(amount.get()));

        orders.setUserName(user.getName());

        orders.setConsignee(addressBook.getConsignee());

        orders.setPhone(addressBook.getPhone());

        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));



        this.save(orders);
        //向订单明细表插入数据

        orderDetailService.saveBatch(orderDetails);

        //清空购物车
        shoppingCartService.remove(queryWrapper);
    }
}
