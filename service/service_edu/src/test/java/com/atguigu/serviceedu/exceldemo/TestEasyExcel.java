package com.atguigu.serviceedu.exceldemo;

import com.alibaba.excel.EasyExcel;
import com.atguigu.serviceedu.exceldemo.DemoData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    @Test
    public void writeExcel(){
        //实现excel的写操作
        //1设置写入地址和文件名
        String filename = "D:\\test\\1128\\write.xlsx";
        //2调方法实现写操作
        //write方法两个参数：1,文件路径名称；2实体类class
        EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite( getData());

    }
    //创建返回list集合方法
    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("zhang"+i);
            list.add(data);
        }
        return list;
    }

}
