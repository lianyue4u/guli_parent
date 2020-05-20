package com.atguigu.serviceedu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.excel.ExcelSubjectData;
import com.atguigu.serviceedu.entity.vo.OneSubjectVo;
import com.atguigu.serviceedu.entity.vo.TwoSubjectVo;
import com.atguigu.serviceedu.listener.ExcelListener;
import com.atguigu.serviceedu.mapper.EduSubjectMapper;
import com.atguigu.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-04-29
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void importSubjectData(MultipartFile file, EduSubjectService eduSubjectService) {

        try {
            //输入流
            InputStream in = file.getInputStream();
            //调用方法读取文件
            EasyExcel.read(in, ExcelSubjectData.class,
                    new ExcelListener(eduSubjectService)).sheet().doRead();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    /*查询课程分类*/
   /* @Override
    public List<OneSubjectVo> getAllSubject() {
        //获取一级分类数据
        QueryWrapper<EduSubject> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id","0");
        List<EduSubject> subjectList1 = baseMapper.selectList(wrapper1);

        //获取二级分类数据
        QueryWrapper<EduSubject> wrapper2 = new QueryWrapper<>();
        wrapper2.ne("parent_id","0");
        List<EduSubject> subjectList2 = baseMapper.selectList(wrapper1);

        //创建最终返回的数据集合
        List<OneSubjectVo> finalSubjectList = new ArrayList<>();

        //封装一级分类、二级分类
        //遍历分类并转化为vo
        //存数据到volist
        for (EduSubject eduSubject : subjectList1) {
            OneSubjectVo oneSubjectVo = new OneSubjectVo();
          *//*  oneSubjectVo.setId(eduSubject.getId());
            oneSubjectVo.setTitle(eduSubject.getTitle());*//*
          //用工具赋值
            BeanUtils.copyProperties(eduSubject,oneSubjectVo);

            //二级fenlei
            List<TwoSubjectVo> twoSubjectVoList  = new ArrayList<>();
            for (EduSubject subject : subjectList2) {
                TwoSubjectVo twoSubjectVo = new TwoSubjectVo();
                //进行判断一二级的关联
                if (eduSubject.getId().equals(subject.getParentId())){
                    //转化为vo
                    BeanUtils.copyProperties(subject,twoSubjectVo);
                    twoSubjectVoList.add(twoSubjectVo);
                }

            }
            //二级vo集合存入
            oneSubjectVo.setChildren(twoSubjectVoList);
            finalSubjectList.add(oneSubjectVo);
        }


        return finalSubjectList;
    }*/
    //查询课程分类
    @Override
    public List<OneSubjectVo> getAllSubject() {
        //1获取一级分类数据
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);
        //2获取二级分类数据
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        //3创建最终返回数据集合
        List<OneSubjectVo> finalSubjectList = new ArrayList<>();
        //4封装一级分类
        //4.1遍历一级分类
        for (int i = 0; i <oneSubjectList.size() ; i++) {
            EduSubject oneSubject = oneSubjectList.get(i);
            //4.2 EduSubject转化成OneSubjectVo
            OneSubjectVo oneSubjectVo = new OneSubjectVo();
//            oneSubjectVo.setId(oneSubject.getId());
//            oneSubjectVo.setTitle(oneSubject.getTitle());
            //把oneSubject里的值复制oneSubjectVo
            BeanUtils.copyProperties(oneSubject,oneSubjectVo);
            //4.3 把数据存入finalSubjectList
            finalSubjectList.add(oneSubjectVo);
            //4.4创建集合封装二级分类
            List<TwoSubjectVo> twoVoList = new ArrayList<>();
            //5封装二级分类
            for (int m = 0; m <twoSubjectList.size() ; m++) {
                EduSubject twoSubject = twoSubjectList.get(m);
                //5.1判断，判断一级分类id和二级分类的parentid比较
                if(oneSubject.getId().equals(twoSubject.getParentId())){
                    //5.2 twoSubject转化TwoSubjectVo
                    TwoSubjectVo twoSubjectVo = new TwoSubjectVo();
                    BeanUtils.copyProperties(twoSubject,twoSubjectVo);
                    twoVoList.add(twoSubjectVo);
                }

            }

            //6 二级vo集合存入一级分类vo里
            oneSubjectVo.setChildren(twoVoList);

        }

        return finalSubjectList;
    }
}
