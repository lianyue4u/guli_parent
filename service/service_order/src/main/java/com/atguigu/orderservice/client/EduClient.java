package com.atguigu.orderservice.client;

import com.atguigu.commonutils.vo.CourseWebVoPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-edu")
public interface EduClient {
    @GetMapping("/serviceedu/courseapi/getCourseInfoPay/{id}")
    public CourseWebVoPay getCourseInfoPay(@PathVariable("id") String id);//查询课程基本信息
}
