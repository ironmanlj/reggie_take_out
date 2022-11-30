package com.example.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reggie_take_out.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @创建人 ironman_lj
 * @创建时间 2022-11-30
 * @描述
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
