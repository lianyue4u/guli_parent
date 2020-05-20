package com.atguigu.serviceedu.service;

import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.vo.OneSubjectVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-04-29
 */
public interface EduSubjectService extends IService<EduSubject> {

    void importSubjectData(MultipartFile file, EduSubjectService eduSubjectService);

    List<OneSubjectVo> getAllSubject();

}
