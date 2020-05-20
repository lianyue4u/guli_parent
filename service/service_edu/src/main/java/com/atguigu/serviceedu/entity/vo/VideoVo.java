package com.atguigu.serviceedu.entity.vo;

import lombok.Data;

import java.io.PipedReader;

@Data
public class VideoVo {
    private String id;
    private String title;
    //视频id必须与eduvideo字段名一致
    private String videoSourceId;
}
