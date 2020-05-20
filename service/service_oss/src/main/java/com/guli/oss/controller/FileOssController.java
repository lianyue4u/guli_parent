package com.guli.oss.controller;

import com.atguigu.commonutils.R;
import com.guli.oss.ossservice.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "上传文件管理")
@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class FileOssController {

    @Autowired
    private FileService fileService;


    @ApiOperation(value = "upload file")
    @PostMapping("fileUpload")
    public R fileUploadOss(MultipartFile file){
        //得到上传文件
        //获取文件上传到阿里云oss
        //返回oss地址
        String url = fileService.uploadFileOss(file);
        return  R.ok().data("url",url);
    }


}
