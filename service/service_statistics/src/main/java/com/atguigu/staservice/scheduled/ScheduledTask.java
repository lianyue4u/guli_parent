package com.atguigu.staservice.scheduled;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    /*@Scheduled(cron = "0/5")*/
    @Autowired
    private StatisticsDailyService staService;

    /**
     * 测试
     * 每天七点到二十三点每五秒执行一次
     */

 /*   @Scheduled(cron = "0/5 * * * * ?")
    public void task1() {
        System.out.println("*********++++++++++++*****执行了");
    }*/

    //在每天凌晨1点，统计前一天注册人数
    @Scheduled(cron = "0 0 1 * * ?")
    public void  createData(){
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), 1));
        staService.createData(day);

    }

}
