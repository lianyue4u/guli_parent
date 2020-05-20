package com.atguigu.ucenter.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.vo.UcenterMemberPay;
import com.atguigu.ucenter.entity.Member;
import com.atguigu.ucenter.entity.vo.RegisterVo;
import com.atguigu.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-09
 */
@Api(description = "会员管理")
@CrossOrigin
@RestController
@RequestMapping("/ucenter/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "登录")
    @PostMapping("login")
    public  R loginUser(@RequestBody Member member){
        String token = memberService.login(member);
        return R.ok().data("token",token);
    }

    @ApiOperation(value = "根据token获取用户信息")
    @PostMapping("getInfoToken")
    public R getInfoToken(HttpServletRequest request){
        //根据jwt获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //根据用户id获取用户信息
        Member ucenterMember = memberService.getById(memberId);
        return R.ok().data("member",ucenterMember);
    }

    @ApiOperation(value = "根据用户id获取用户信息订单远程支付")
    @GetMapping("getUcenterPay/{memberId}")
    public UcenterMemberPay getUcenterPay(@PathVariable String memberId){
        Member member = memberService.getById(memberId);
        UcenterMemberPay ucenterMemberPay = new UcenterMemberPay();
        BeanUtils.copyProperties(member,ucenterMemberPay);
        return ucenterMemberPay;
    }

    @ApiOperation(value = "统计某一天注册人数,统计分析远程调用")
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count = memberService.countRegister(day);
        return R.ok().data("registerCount",count);
    }


}

