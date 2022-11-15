package com.example.reggie_take_out.filter;


import com.alibaba.fastjson.JSON;
import com.example.reggie_take_out.common.BaseContext;
import com.example.reggie_take_out.common.Context;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Component
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        //获取请求的url
        String requestURI=request.getRequestURI();

        log.info("拦截到请求：{}",requestURI);
        //设定不需要拦截
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        //检测是否需要放行
        boolean check = check(urls, requestURI);
        if(check){
            log.info("本次请求不需要处理");
            filterChain.doFilter(request,response);
            return;
        }


        //判断是否完成登录
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录 {}",request.getSession().getAttribute("employee"));
            Long empId=(Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }
        //如果没有登录信息，则通过输出流的方式向客户端传递信息
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(Context.error("NOTLOGIN")));
        return;

    }

    public boolean check(String[] urls,String requestURI){
        for (String url:urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
