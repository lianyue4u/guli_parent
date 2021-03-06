package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.ValidateCodeUtils;
import com.atguigu.msmservice.service.MsmService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(description = "短信发送管理")
@RestController
@CrossOrigin
@RequestMapping("/edumsm/msm")
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @ApiOperation(value = "根据手机号发送验证码")
    @GetMapping("/send/{phone}")
    public R sendMsmPhone(@PathVariable String phone){
        //1 redis里根据手机号获取数据
        String rphone = redisTemplate.opsForValue().get(phone);

        //2 如果能取出数据直接返回
        if (!StringUtils.isEmpty(rphone)){
            return R.ok();
        }
        //3 如果取不出直接调接口发送短信
        String code = ValidateCodeUtils.generateValidateCode4String(6);
        Map<String, String> map = new HashMap<>();
        map.put("code",code);
        boolean isSuccess =   msmService.sendMsm(phone,map);
        if (isSuccess){
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().message("发送短信失败");
        }

    }


}
