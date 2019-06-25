package com.ughen.service;


import com.ughen.util.JsonResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description:  文件上传
 * @Author: Yonghong Zhang
 * @Date: 2019/2/12
 * @Time: 14:46
 */
public interface FileUploadService {

   JsonResult uploadFile(MultipartFile fileupload, String uploadDirection,
                         String bucketName);
}
