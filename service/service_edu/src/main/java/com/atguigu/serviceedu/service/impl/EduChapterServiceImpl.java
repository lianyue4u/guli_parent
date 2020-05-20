package com.atguigu.serviceedu.service.impl;

import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.entity.vo.ChapterVo;
import com.atguigu.serviceedu.entity.vo.VideoVo;
import com.atguigu.serviceedu.mapper.EduChapterMapper;
import com.atguigu.serviceedu.service.EduChapterService;
import com.atguigu.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-04
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {


    @Autowired
    private EduVideoService videoService;


    @Override
    public List<ChapterVo> getChapterVideoById(String courseId) {

        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> chapterList = baseMapper.selectList(wrapperChapter);

        //2根据课程id查询小节信息
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> videoList = videoService.list(wrapperVideo);

        //3创建集合，用于封装结果
        List<ChapterVo> finalList = new ArrayList<>();
        //4遍历章节封装
        for (int i = 0; i <chapterList.size() ; i++) {
            //4.1取得每个章节信息
            EduChapter eduChapter = chapterList.get(i);
            //4.2 eduChapter转化ChapterVo
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalList.add(chapterVo);

            //5遍历小节进行封装
            List<VideoVo> videoVoList = new ArrayList<>();
            for (int m = 0; m <videoList.size() ; m++) {
                EduVideo eduVideo = videoList.get(m);
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVoList.add(videoVo);
                }
            }

            chapterVo.setChildren(videoVoList);

        }

        return finalList;
    }

}

