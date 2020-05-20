package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(description = "模拟用户登录")
@RestController
@RequestMapping("/eduuser")
//@CrossOrigin
public class EduLogincontroller {

    @ApiOperation(value = "模拟登陆")
    @PostMapping("login")
    public R login(){
        return R.ok().data("token","admin");
    }

    @ApiOperation(value = "模拟获取信息")
    @GetMapping("info")
    public  R info(){
        return R.ok().data("roles","admin")
                .data("name","admin")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }




}
