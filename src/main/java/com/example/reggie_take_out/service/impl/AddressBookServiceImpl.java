package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.AddressBook;
import com.example.reggie_take_out.mapper.AddressBookMapper;
import com.example.reggie_take_out.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @创建人 ironman_lj
 * @创建时间 2022-12-02
 * @描述
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
