package com.atguigu.serviceedu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.excel.ExcelSubjectData;
import com.atguigu.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class ExcelListener extends AnalysisEventListener<ExcelSubjectData> {

    public EduSubjectService eduSubjectService;

    public ExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    public ExcelListener() {
    }

    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
            //判断数据
        if (excelSubjectData==null){
            throw  new GuliException(20001,"文件数据为空");
        }
        //获取一级分类的id
        //判断二级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(eduSubjectService, excelSubjectData.getOneSubjectName());

        if (existOneSubject==null){
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(excelSubjectData.getOneSubjectName());
            eduSubjectService.save(existOneSubject);
        }
        //判断二级
        String pid = existOneSubject.getId();
        EduSubject exist2Subject = this.exist2Subject(eduSubjectService, excelSubjectData.getTwoSubjectName(), pid);

    }

    //判断一级分类是否重复
    private EduSubject existOneSubject(EduSubjectService eduSubjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        //wrapper和sql字段对应
        wrapper.eq("title",name);
        wrapper.eq("parent_id",0);
        EduSubject oneSubject = eduSubjectService.getOne(wrapper);
        return oneSubject;
    }


    //判断er级分类是否重复
    private EduSubject exist2Subject(EduSubjectService eduSubjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        //wrapper和sql字段对应
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject twoSubject = eduSubjectService.getOne(wrapper);
        return twoSubject;
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
