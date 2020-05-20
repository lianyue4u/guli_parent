package com.atguigu.wxlogin.service;

import com.atguigu.wxlogin.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-11
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    UcenterMember getWxInfoByOpenid(String openid);
}
