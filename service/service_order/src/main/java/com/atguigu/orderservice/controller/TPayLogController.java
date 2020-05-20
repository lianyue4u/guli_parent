package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.orderservice.service.TPayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-15
 */
@Api(description = "支付管理")
@CrossOrigin
@RestController
@RequestMapping("/orderservice/paylog")
public class TPayLogController {

    @Autowired
    private TPayLogService payLogService;

    @ApiOperation(value = "根据订单号生成支付二维码")
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        Map map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    @ApiOperation(value = "查询支付状态")
    @GetMapping("queryOrderStatus/{orderNo}")
    public R queryOrderStatus(@PathVariable String orderNo){
        Map<String, String> map = payLogService.queryPayStatus(orderNo);

        if (map == null) {//出错
            return R.error().message("支付出错");
        }
        //根据微信支付SDK查询是否成功
        if (map.get("trade_state").equals("SUCCESS")) {//如果成功
            //更改订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中");


    }

}

