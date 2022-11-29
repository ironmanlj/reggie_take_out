package com.example.reggie_take_out.dto;

import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建人 ironman_lj
 * @创建时间 2022-11-29
 * @描述
 */

@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors=new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
