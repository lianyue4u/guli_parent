package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.client.VodClient;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-04
 */
@RestController
@CrossOrigin
@RequestMapping("/serviceedu/eduvideo")
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    @ApiOperation(value = "新增小节信息")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }

    @ApiOperation(value = "删除小节信息")
    @DeleteMapping("{id}")
//TODO 删除小节，需要删除阿里云对应视频
    public R deleteVideo(@PathVariable String id){
        EduVideo eduVideo = videoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)){
            vodClient.removeVideo(videoSourceId);
        }
        videoService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据id查询小节信息")
    @GetMapping("{id}")
    public R getVideoInfo(@PathVariable String id){
        EduVideo eduVideo = videoService.getById(id);
        return R.ok().data("video",eduVideo);
    }

    @ApiOperation(value = "修改小节信息")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        videoService.updateById(eduVideo);
        return R.ok();
    }










}

