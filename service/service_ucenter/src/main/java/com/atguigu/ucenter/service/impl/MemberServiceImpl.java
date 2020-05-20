package com.atguigu.ucenter.service.impl;

import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.utils.MD5;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.ucenter.entity.Member;
import com.atguigu.ucenter.entity.vo.RegisterVo;
import com.atguigu.ucenter.mapper.MemberMapper;
import com.atguigu.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-09
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void register(RegisterVo registerVo) {
        //获取注册信息，进行校验
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();
        //校验参数
        if(StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new GuliException(20001,"error");
        }
        //校验校验验证码
        //从redis获取发送的验证码
        String mobilecode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(mobilecode)){
            throw new GuliException(20001,"error");
        }
        Integer count = baseMapper.selectCount(new QueryWrapper<Member>().eq("mobile", mobile));
        if (count.intValue()>0){
            throw new GuliException(20001,"error");
        }
        //添加注册信息到数据库
        Member member = new Member();
        BeanUtils.copyProperties(registerVo,member);
        String passwordBefore = member.getPassword();
        String passwordAfter = MD5.encrypt(passwordBefore);
        member.setPassword(passwordAfter);
        member.setIsDisabled(false);
        member.setAvatar("https://guli-file-190513.oss-cn-beijing.aliyuncs.com/avatar/default.jpg");
        int insert = baseMapper.insert(member);
        if(insert==0){
            throw  new GuliException(20001,"注册失败");
        }



    }

    @Override
    public String login(Member member) {
        //判断参数是否为空
        String mobile = member.getMobile();
        String password = member.getPassword();
        if (StringUtils.isEmpty(mobile)||
                StringUtils.isEmpty(password)){
            throw new GuliException(20001,"用户名或密码错误");
        }
        //根据手机号查询
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Member ucenterMember = baseMapper.selectOne(wrapper);
        if (ucenterMember==null){
            throw new GuliException(20001,"用户名或密码错误");
        }
        String passwordDB = ucenterMember.getPassword();
        //验证密码
        String encrypt = MD5.encrypt(password);
        if (!encrypt.equals(passwordDB)){
            throw new GuliException(20001,"用户名或密码错误");
        }
        //判断用户是否注销
        if (ucenterMember.getIsDisabled()){
            throw new GuliException(20001,"用户名或密码错误");
        }
        //生成token字符串
        String token = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
        return token;
    }

    @Override
    public Integer countRegister(String day) {

        Integer count = baseMapper.countRegister(day);
        return  count;
    }


}
