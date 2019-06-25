package com.ughen.service.impl;


import com.ughen.constants.ResultCode;
import com.ughen.constants.UploadDirection;
import com.ughen.service.FileUploadService;
import com.ughen.util.FileUtil;
import com.ughen.util.JsonResult;
import com.ughen.util.StringUtil;
import com.ughen.util.aliyun.oss.AliOssUtil;
import com.ughen.util.qiniuyun.QiNiuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @Description:
 * @Author: Yonghong Zhang
 * @Date: 2019/2/12
 * @Time: 14:56
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {


    private static Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);


    @Value("${file.default.bucketName}")
    private String defaultBucket;

    /**
     * @param fileupload
     * @param uploadDirection aliyun:阿里云oss local:本地服务器 qiniu:七牛云 默认本地空间
     * @param bucketName
     * @return
     */
    @Override
    public JsonResult uploadFile(MultipartFile fileupload, String uploadDirection, String bucketName) {
        String url = "";
        if (fileupload == null) {
            return JsonResult.build(ResultCode.FILE_NULL);
        }
        if (StringUtil.isEmpty(bucketName)) {
            bucketName = defaultBucket;
        }
        try {
            if (UploadDirection.ALIYUN.getValue().equals(uploadDirection)) {
                // 上传到阿里云
                url = AliOssUtil.uploadOSS(fileupload, bucketName);
            } else if (UploadDirection.LOCAL.getValue().equals(uploadDirection) || StringUtil.isEmpty(uploadDirection)) {
                //上传到本地
                String fileName = fileupload.getOriginalFilename();
                url = FileUtil.uploadFile(fileupload.getBytes(), fileName);
            } else if (UploadDirection.QINIU.getValue().equals(uploadDirection)) {
                //上传到七牛云
                InputStream inputStream = fileupload.getInputStream();
                url = QiNiuUtil.upload(inputStream, fileupload.getOriginalFilename(), bucketName);
            }
            return JsonResult.build(ResultCode.SUCCESS, url);
        } catch (Exception e) {
            logger.error("FileUploadServiceImpl | uploadFile | 文件上传失败", e);
            return JsonResult.build(ResultCode.FILE_ERROR);
        }
    }
}
