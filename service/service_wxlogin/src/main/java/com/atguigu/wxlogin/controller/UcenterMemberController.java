package com.atguigu.wxlogin.controller;


import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.wxlogin.entity.UcenterMember;
import com.atguigu.wxlogin.service.UcenterMemberService;
import com.atguigu.wxlogin.utils.ConstantPropertiesUtil;
import com.atguigu.wxlogin.utils.HttpClientUtils;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-11
 */
@Controller
@CrossOrigin
@Api(description = "微信扫码登录")
@RequestMapping("/api/ucenter/wx")
public class UcenterMemberController {


    @Autowired
    private UcenterMemberService memberService;

    @ApiOperation(value = "生成微信登录二维码")
    @GetMapping("login")
    public String login(){
        //方案一：拼接url字符串
        //方案二：
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //回调地址
        String wxOpenRedirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            URLEncoder.encode(wxOpenRedirectUrl,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //传递参数
        String wxURL = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                wxOpenRedirectUrl,
                "imhelen"
                );
        return "redirect:"+wxURL;
    }

    //微信扫码后回调方法
    @ApiOperation(value = "微信扫码后回调方法")
    @GetMapping("callback")
    public String callback(String code, String state){

        //得到授权临时票据code
        System.out.println(code);
        System.out.println(state);

        //从redis中将state获取出来，和当前传入的state作比较
        //如果一致则放行，如果不一致则抛出异常：非法访问

        //向认证服务器发送请求换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessToken=============" + result);
        } catch (Exception e) {
            throw new GuliException(20001, "获取access_token失败");
        }

        //解析json字符串
        Gson gson = new Gson();
        HashMap map = gson.fromJson(result, HashMap.class);
        String accessToken = (String)map.get("access_token");
        String openid = (String)map.get("openid");

        //查询数据库当前用用户是否曾经使用过微信登录
        UcenterMember member = memberService.getWxInfoByOpenid(openid);
        if(member == null){
            System.out.println("新用户注册");

            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultUserInfo==========" + resultUserInfo);
            } catch (Exception e) {
                throw new GuliException(20001, "获取用户信息失败");
            }

            //解析json
            HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
            String nickname = (String)mapUserInfo.get("nickname");
            String headimgurl = (String)mapUserInfo.get("headimgurl");

            //向数据库中插入一条记录
            member = new UcenterMember();
            member.setNickname(nickname);
            member.setOpenid(openid);
            member.setAvatar(headimgurl);
            memberService.save(member);
        }
        String token = JwtUtils.getJwtToken(member.getId(),member.getNickname());

        //TODO 登录

        return "redirect:http://localhost:3000?token="+token;
    }

}

