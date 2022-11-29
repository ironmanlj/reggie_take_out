package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.entity.Setmeal;

import java.util.List;

/**
 * @创建人 ironman_lj
 * @创建时间 2022-11-21
 * @描述
 */
public interface SetmealService extends IService<Setmeal> {
    //新增套餐
    public void saveWithDish(SetmealDto setmealDto);
    //删除套餐，以及删除与菜品的关联数据
    public void removeWithDish(List<Long> ids);

}
