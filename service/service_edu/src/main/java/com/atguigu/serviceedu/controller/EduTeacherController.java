package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.entity.vo.TeacherQuery;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-04-24
 */
@Api(description="讲师管理")
@RestController
@RequestMapping("/serviceedu/eduteacher")
public class EduTeacherController {

    //引入service
    @Autowired
    private EduTeacherService eduTeacherService;


    //查询所有讲师
    @ApiOperation(value = "所有讲师列表")
    @GetMapping
    public R getAllTeacher(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    //根据id逻辑删除
    @ApiOperation(value = "根据ID逻辑删除讲师")
    @DeleteMapping("{id}")
    public R deleteTeacherId(@PathVariable String id){
        boolean remove = eduTeacherService.removeById(id);
        return  R.ok();
    }

    @ApiOperation(value = "分页查询讲师列表")
    @GetMapping("getTeacherPage/{current}/{limit}")
    public R getTeacherPage(@PathVariable Long current,@PathVariable Long limit){
        //1创建page对象
        Page<EduTeacher> page = new Page<>(current,limit);
        //2调用方法获得数据
        eduTeacherService.page(page,null);
        //3从page里获得数据
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        //4返回数据
//        Map<String,Object> map = new HashMap<>();
//        map.put("total",total);
//        map.put("items",records);
//        return R.ok().data(map);

        return R.ok().data("total",total).data("items",records);
    }

    @ApiOperation(value = "带条件分页查询讲师列表")
    @PostMapping("getTeacherPageVo/{current}/{limit}")
    public R getTeacherPageVo(@PathVariable Long current,
                              @PathVariable Long limit,
                              @RequestBody TeacherQuery teacherQuery){
        //@RequestBody必须使用PostMapping
        // @RequestBody可以把json对象转化成vo
        //1创建page对象
        Page<EduTeacher> page = new Page<>(current,limit);
        //1.5从teacherQuery获取值拼接条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }

        //2调用方法获得数据
        eduTeacherService.page(page,wrapper);
        //3从page里获得数据
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        //4返回数据
//        Map<String,Object> map = new HashMap<>();
//        map.put("total",total);
//        map.put("items",records);
//        return R.ok().data(map);

        return R.ok().data("total",total).data("items",records);
    }


    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody  EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }else {
            return R.error();
        }
    }


    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("getTeacherById/{id}")
    public R getTeacherById(@PathVariable String id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("eduTeacher",eduTeacher);
    }

    @ApiOperation(value = "修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody  EduTeacher eduTeacher){
        boolean update = eduTeacherService.updateById(eduTeacher);
        if(update){
            return R.ok();
        }else {
            return R.error();
        }
    }

}

