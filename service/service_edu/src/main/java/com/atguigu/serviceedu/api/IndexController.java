package com.atguigu.serviceedu.api;

import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "首页展示")
@RestController
@CrossOrigin
@RequestMapping("/serviceedu/index")
public class IndexController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation(value = "首页展示")
    @GetMapping
    public R getIndexData(){
        //查询8条记录以及4位讲师
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> eduCourseList = courseService.list(wrapper);
        //查询4位讲师
        QueryWrapper<EduTeacher> wrapperteacher = new QueryWrapper<>();
        wrapperteacher.orderByDesc("id");
        wrapperteacher.last("limit 4");
        List<EduTeacher> eduTeacherList = teacherService.list(wrapperteacher);
        //查询4位讲师
        return R.ok().data("hotCourse",eduCourseList)
                .data("teacher",eduTeacherList);

    }





}
