package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.Context;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.entity.Category;
import com.example.reggie_take_out.entity.Setmeal;
import com.example.reggie_take_out.entity.SetmealDish;
import com.example.reggie_take_out.service.CategoryService;
import com.example.reggie_take_out.service.SetmealDishService;
import com.example.reggie_take_out.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Context<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);

        return Context.success("新增套餐信息成功");
    }

    @GetMapping("/page")
    public Context<Page> page(int page,int pageSize,String name){
        //分页构造器
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        Page<SetmealDto> pageDtoInfo=new Page<>();

        //创建条件
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.like(name!=null,Setmeal::getName,name);

        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //拷贝
        BeanUtils.copyProperties(pageInfo,pageDtoInfo,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list=records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(item,setmealDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);

            if (category!=null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        pageDtoInfo.setRecords(list);

        return Context.success(pageDtoInfo);
    }

    @DeleteMapping
    public Context<String> delete(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return Context.success("删除成功");
    }


    @PostMapping("/status/{value}")
    public Context<String> changeStatus(@PathVariable Integer value,@RequestParam List<Long> ids){
        //新建更新器
        UpdateWrapper<Setmeal> updateWrapper=new UpdateWrapper<>();
        //根据ids选择更新的项
        updateWrapper.in("id",ids);
        //更新状态
        updateWrapper.set("status",value);
        //写入数据库
        setmealService.update(updateWrapper);

        return Context.success("修改套餐状态成功");
    }

    //展示套餐列表
    @GetMapping("/list")
    public Context<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());

        queryWrapper.eq(Setmeal::getStatus,1);

        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        return Context.success(setmealService.list(queryWrapper));
    }
}
