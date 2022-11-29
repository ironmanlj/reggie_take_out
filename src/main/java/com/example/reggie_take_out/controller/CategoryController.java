package com.example.reggie_take_out.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.Context;
import com.example.reggie_take_out.entity.Category;
import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /*
    设置菜品种类和套餐种类
     */
    @PostMapping
    public Context<String> save(@RequestBody Category category){
        log.info("category:{}",category);

        categoryService.save(category);
        return Context.success("种类已插入");
    }

    @GetMapping ("/page")
    public Context<Page> page(int page,int pageSize){
        //分页构造器
        Page<Category> pageinfo=new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        //执行分页查询
        categoryService.page(pageinfo,queryWrapper);

        return Context.success(pageinfo);
    }

    @DeleteMapping
    public Context<String> delete(Long id){
        System.out.println(id);

        categoryService.remove(id);
        //categoryService.removeById(id);
        return Context.success("删除成功");
    }

    @GetMapping("/list")
    public Context<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //排序
        queryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        //查询
        List<Category> list = categoryService.list(queryWrapper);

        return Context.success(list);
    }

    @PutMapping
    public Context<String> update(@RequestBody Category category){
        log.info("修改分类信息");
        categoryService.updateById(category);
        return Context.success("修改分类信息成功");
    }
}
