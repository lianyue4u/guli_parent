package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-16
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void createData(String day) {
        //1、远程调用获得注册人数
        R r = ucenterClient.countRegister(day);
        Integer registerCount = (Integer) r.getData().get("registerCount");
        //2、添加之前，根据统计日期删除表内同日期数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        baseMapper.delete(wrapper);
        //3、添加新数据
        //获取统计信息
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO

        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(registerCount);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);
        baseMapper.insert(daily);

    }

    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {

        //1根据时间范围查询数据（制定查询字段）
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        wrapper.select("date_calculated",type);
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);
        //2 封装数据x轴数据（数据日期），y轴（指标数据）
        List<String> dateCalculatedList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();
        //2.1遍历数据
        for (int i = 0; i < staList.size(); i++) {
            StatisticsDaily sta = staList.get(i);
            //2.2封装x轴
            String dateCalculated = sta.getDateCalculated();
            dateCalculatedList.add(dateCalculated);
            //2.3封装y轴
            switch (type){
                case "login_num":
                    Integer loginNum = sta.getLoginNum();
                    dataList.add(loginNum);
                    break;
                case "register_num":
                    Integer registerNum = sta.getRegisterNum();
                    dataList.add(registerNum);
                    break;
                case "video_view_num":
                    Integer videoViewNum = sta.getVideoViewNum();
                    dataList.add(videoViewNum);
                    break;
                case "course_num":
                    Integer courseNum = sta.getCourseNum();
                    dataList.add(courseNum);
                    break;
                default:
                    break;
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("dateCalculatedList",dateCalculatedList);
        map.put("dataList",dataList);
        return map;
    }
}
