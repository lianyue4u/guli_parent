package com.atguigu.serviceedu.api;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.vo.CourseWebVoPay;
import com.atguigu.serviceedu.client.OrderClient;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.vo.ChapterVo;
import com.atguigu.serviceedu.entity.vo.CourseQueryVo;
import com.atguigu.serviceedu.entity.vo.CourseWebVo;
import com.atguigu.serviceedu.service.EduChapterService;
import com.atguigu.serviceedu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description="前台课程展示")
@RestController
@RequestMapping("/serviceedu/courseapi")
@CrossOrigin
public class CourseApiController {
    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    @ApiOperation(value = "前台课程的条件分页查询功能")
    @PostMapping("getFrontCourseList/{current}/{limit}")
    public R getFrontCourseList(@PathVariable long current,
                                @PathVariable long limit,
                                @RequestBody(required = false)
                                CourseQueryVo courseQueryVo){
        Page<EduCourse> page = new Page<>(current, limit);
        Map<String,Object> map = courseService.getCoursePageList(page,courseQueryVo);
        return R.ok().data(map);
    }

    //根据id查询课程详情
    @ApiOperation(value = "根据id查询课程详情")
    @PostMapping("getCourseInfo/{id}")
    public R getCourseInfo(@PathVariable String id, HttpServletRequest request){
        CourseWebVo courseWebVo = courseService.getCourseWebVo(id);
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoById(id);
        //查询课程是否被购买
        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println("memberIdByJwtToken==>"+memberIdByJwtToken);
        boolean buyCourse = orderClient.isBuyCourse(id, memberIdByJwtToken);

        return R.ok().data("courseWebVo",courseWebVo)
                .data("chapterVideoList",chapterVideoList)
                .data("isBuyCourse",buyCourse);

    }

    //模块间对接，返回通用vo
    @ApiOperation(value = "根据课程id查询课程信息支付订单远程调用")
    @GetMapping("getCourseInfoPay/{id}")
    public CourseWebVoPay getCourseInfoPay(@PathVariable String id){
        //查询课程基本信息
        CourseWebVo courseWebVo = courseService.getCourseWebVo(id);
        CourseWebVoPay courseWebVoPay = new CourseWebVoPay();
        BeanUtils.copyProperties(courseWebVo,courseWebVoPay);
        return  courseWebVoPay;

    }



}
