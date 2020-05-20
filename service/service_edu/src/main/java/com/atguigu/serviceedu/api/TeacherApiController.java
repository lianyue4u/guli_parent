package com.atguigu.serviceedu.api;

import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "前台讲师展示")
@RestController
@CrossOrigin
@RequestMapping("/serviceedu/teacherapi")
public class TeacherApiController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    //分页查询功能
    @ApiOperation(value = "前台讲师展示")
    @GetMapping("getFrontTeacherList/{current}/{limit}")
    public R getFrontTeacherList(
            @PathVariable long current,
            @PathVariable long limit
    ){
        Page<EduTeacher> pageParam = new Page<>(current, limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        /*baseMapper.selectPage(pageParam, wrapper);*/
        teacherService.page(pageParam,wrapper);
        List<EduTeacher> records = pageParam.getRecords();
        long currentPage = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return R.ok().data(map);

    }

    @ApiOperation(value = "根据id查询讲师详情")
    @GetMapping("getTeacherInfo/{id}")
    public R getTeacherInfo(@PathVariable String id){
        //查询讲师基本信息
        EduTeacher eduTeacher = teacherService.getById(id);
        //讲师基本信息
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",id);
        List<EduCourse> courseList = courseService.list(wrapper);
        return R.ok().data("eduTeacher",eduTeacher).data("courseList",courseList);
    }



}
