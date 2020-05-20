package com.atguigu.ucenter.service;

import com.atguigu.ucenter.entity.Member;
import com.atguigu.ucenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-09
 */
public interface MemberService extends IService<Member> {

    void register(RegisterVo registerVo);

    String login(Member member);

    Integer countRegister(String day);
}
