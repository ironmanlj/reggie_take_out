package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.mapper.DishMapper;
import com.example.reggie_take_out.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @创建人 ironman_lj
 * @创建时间 2022-11-21
 * @描述
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
