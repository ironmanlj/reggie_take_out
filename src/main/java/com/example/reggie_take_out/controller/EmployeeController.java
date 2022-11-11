package com.example.reggie_take_out.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.Context;
import com.example.reggie_take_out.entity.Employee;
import com.example.reggie_take_out.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Context<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //对密码进行MD5处理
        String password=employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        
        //查数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        System.out.println(emp);


        //判断
        if(emp == null){
            return Context.error("登录失败");
        }

        //比对密码
        if(!emp.getPassword().equals(password)){
            return Context.error("密码错误");
        }

        //查看状态
        if(emp.getStatus()==0){
            return Context.error("账号已禁用");
        }

        //
        request.getSession().setAttribute("employee",emp.getId());
        return Context.success(emp);
    }

    @PostMapping("/logout")
    public Context<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Context.success("退出成功");
    }

    @PostMapping()
    public Context<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工");

        //设置初始密码,并进行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        //将数据传给service
        employeeService.save(employee);

        return Context.success("新增员工成功");
    }

    @GetMapping("/page")
    public Context<Page> page(int page,int pageSize,String name){
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo,queryWrapper);

        return Context.success(pageInfo);
    }

    @PutMapping
    public Context<String> Update(HttpServletRequest request,@RequestBody Employee employee){
        Long empId=(Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return Context.success("员工信息修改成功");
    }

}
