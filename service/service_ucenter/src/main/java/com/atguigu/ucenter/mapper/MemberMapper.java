package com.atguigu.ucenter.mapper;

import com.atguigu.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-05-09
 */
public interface MemberMapper extends BaseMapper<Member> {

    Integer countRegister(String day);

}
