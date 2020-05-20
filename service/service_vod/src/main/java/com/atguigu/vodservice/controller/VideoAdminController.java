package com.atguigu.vodservice.controller;

import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.vodservice.service.VideoService;
import com.atguigu.vodservice.utils.AliyunVodSDKUtils;
import com.atguigu.vodservice.utils.ConstantPropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Api(description="阿里云视频点播微服务")
@CrossOrigin //跨域
@RestController
@RequestMapping("/eduvod/video")
public class VideoAdminController {

    @Autowired
    private VideoService videoService;

    @PostMapping("uploadVideo")
    public R uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) throws Exception {

        String videoId = videoService.uploadVideo(file);
        return R.ok().message("视频上传成功").data("videoId", videoId);
    }

    //删除小节删除阿里云视频（feign）
    @DeleteMapping("{videoId}")
    public R removeVideo(
            @ApiParam(name ="videoId",value = "云端视频id",required = true)
            @PathVariable String videoId
    ){
        videoService.removeVideo(videoId);
        return R.ok().message("视频删除成功过");
    }

    @ApiOperation(value = "删除多个视频")
    @DeleteMapping("deleteMoreVideo")
    public R removeVideoList(
            @ApiParam(name = "videoIdList", value = "云端视频id", required = true)
            @RequestParam("videoIdList") List videoIdList){

        videoService.removeVideoList(videoIdList);
        return R.ok().message("视频删除成功");
    }

    @ApiOperation(value = "根据视频id获取视频播放凭证")
    @GetMapping("getPlayAuth/{vid}")
    public R getPlayAuth(@PathVariable String vid){
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
            request.setVideoId(vid);
            //设置凭证有效时间
            //request.setAuthInfoTimeout(200L);
            response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        } catch (ClientException e) {
            throw new GuliException(20001,"获取视频凭证失败");
        }


    }



}
