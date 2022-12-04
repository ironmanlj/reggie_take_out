package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.reggie_take_out.common.BaseContext;
import com.example.reggie_take_out.common.Context;
import com.example.reggie_take_out.entity.AddressBook;
import com.example.reggie_take_out.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @创建人 ironman_lj
 * @创建时间 2022-12-02
 * @描述
 */

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    //新增地址
    @PostMapping
    public Context<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return Context.success(addressBook);
    }

    //设置默认地址

    @PutMapping("/default")
    public Context<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        LambdaUpdateWrapper<AddressBook> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(updateWrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return Context.success(addressBook);
    }


    @GetMapping("/{id}")
    //根据ID查询地址
    public Context<AddressBook> get(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        if(addressBook!=null){
            return Context.success(addressBook);
        }else{
            return Context.error("没有对象");
        }
    }


    //查询默认地址
    @PostMapping("default")
    public Context<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault,1);

        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (addressBook ==null){
            return Context.error("没找到");
        }else {
            return Context.success(addressBook);
        }
    }

    //查询全部地址信息
    @GetMapping("/list")
    public Context<List<AddressBook>> list(AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());


        LambdaQueryWrapper<AddressBook> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(null!=addressBook.getUserId(),AddressBook::getUserId,addressBook.getUserId());

        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        return Context.success(addressBookService.list(queryWrapper));
    }

    //更新地址
    @PutMapping
    public Context<String> update(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return Context.success("更新成功");
    }

    //删除地址
    @DeleteMapping
    public Context<String> delete(Long ids){
        addressBookService.removeById(ids);
        return Context.success("删除成功");
    }
}
