package com.atguigu.serviceedu.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-order")
@Component
public interface OrderClient {

    @GetMapping("/orderservice/order/isBuyCourse/{cid}/{mid}")
    public boolean isBuyCourse(@PathVariable("cid") String cid,
                               @PathVariable("mid") String mid);

    }
