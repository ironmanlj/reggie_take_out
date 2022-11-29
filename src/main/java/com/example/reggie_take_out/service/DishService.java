package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.entity.Dish;

/**
 * @创建人 ironman_lj
 * @创建时间 2022-11-21
 * @描述
 */
public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味 需要操作两张表
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);
}
