package com.example.reggie_take_out.controller;

import com.example.reggie_take_out.common.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @创建人 ironman_lj
 * @创建时间 2022-11-29
 * @描述
 */

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public Context<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求结束后文件会删除
        log.info(file.toString());

        //获取原先文件名
        String originalFilename = file.getOriginalFilename();

        //获取文件后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID生成独特的文件名，防止发生文件覆盖
        String s = UUID.randomUUID().toString();

        String filename=s+substring;

        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //目录不存在
            dir.mkdirs();
        }


        //指定文件夹存放文件
        try {
            file.transferTo(new File(basePath+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Context.success(filename);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //通过文件名称找到文件，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));
            //通过response，将输出流文件写会浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            //读
            response.setContentType("image/jpeg");

            int len=0;
            byte[] bytes = new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
