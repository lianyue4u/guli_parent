package com.atguigu.serviceedu.service;

import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.vo.CourseInfoForm;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.atguigu.serviceedu.entity.vo.CourseQueryVo;
import com.atguigu.serviceedu.entity.vo.CourseWebVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-04-30
 */
public interface EduCourseService extends IService<EduCourse> {

    //

    String addCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfoId(String courseId);

    void updateCourse(CourseInfoForm courseInfoForm);

    CoursePublishVo getCoursePublishVo(String courseId);

    void deleteCourse(String courseId);

    Map<String, Object> getCoursePageList(Page<EduCourse> page, CourseQueryVo courseQueryVo);


    CourseWebVo getCourseWebVo(String id);

}
