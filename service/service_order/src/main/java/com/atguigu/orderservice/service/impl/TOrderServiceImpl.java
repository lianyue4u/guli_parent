package com.atguigu.orderservice.service.impl;

import com.atguigu.commonutils.vo.CourseWebVoPay;
import com.atguigu.commonutils.vo.UcenterMemberPay;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.mapper.TOrderMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.utils.OrderNoUtil;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-15
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private EduClient eduClient;


    @Override
    public String createOrder(String courseId, String memberId) {

        //生成订单号
        String orderNo = OrderNoUtil.getOrderNo();
        //根据课程id查询课程信息，远程调用
        CourseWebVoPay courseInfoPay = eduClient.getCourseInfoPay(courseId);
        //校验数据
        if (courseInfoPay==null){
            throw new GuliException(20001,"课程获取失败");
        }
        //根据用户查询用户信息，远程调用
        UcenterMemberPay ucenterPay = ucenterClient.getUcenterPay(memberId);
        //校验数据
        if (ucenterPay==null){
            throw new GuliException(20001,"用户获取失败");
        }
        //把订单数据插入数据库
        //创建订单
        TOrder order = new TOrder();
        order.setOrderNo(orderNo);
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoPay.getTitle());
        order.setCourseCover(courseInfoPay.getCover());
        order.setTeacherName(courseInfoPay.getTeacherName());
        order.setTotalFee(courseInfoPay.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterPay.getMobile());
        order.setNickname(ucenterPay.getNickname());
        order.setStatus(0);  //0：未支付 1；已支付
        order.setPayType(1); //微信支付
        baseMapper.insert(order);
        return orderNo;
    }
}
