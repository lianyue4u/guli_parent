package com.atguigu.serviceedu.exceldemo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.junit.Test;

public class ExcelListener extends AnalysisEventListener<DemoData> {

    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println("*****" + demoData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Test
    public void readExcel() {
        //实现excel的读操作
        //1设置写入地址和文件名
        String filename = "D:\\test\\1128\\write.xlsx";
        EasyExcel.read(filename, DemoData.class, new ExcelListener()).sheet().doRead();
    }


}
