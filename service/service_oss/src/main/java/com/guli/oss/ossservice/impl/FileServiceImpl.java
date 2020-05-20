package com.guli.oss.ossservice.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.servicebase.handler.GuliException;
import com.guli.oss.ossservice.FileService;
import com.guli.oss.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadFileOss(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtil.END_POINT;
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 上传文件流。
            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            filename = uuid+filename;
            //文件分类优化
            //定义文件ml，例如目录/2020/4/29
            String path = new DateTime().toString("yyyy/MM/dd");
            filename = path + "/" +filename;
            ossClient.putObject(ConstantPropertiesUtil.BUCKET_NAME, filename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();
            //拼接url
            //项目名-节点名-文件名
            String url = "https://" + bucketName + "." +
                    endpoint + "/" + filename;
            return url;

        } catch (Exception e) {
            throw new GuliException(20001, "上传失败");
        }


    }
}
