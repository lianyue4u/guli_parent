package com.guli.oss.ossservice;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFileOss(MultipartFile file);
}
