package com.ughen.controller;

import com.ughen.constants.ResultCode;
import com.ughen.jwt.annotation.UserLoginToken;
import com.ughen.service.FileUploadService;
import com.ughen.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储管理
 * Author:@Yonghong Zhang
 * Date: 15:54 2019/1/21
 */
@Controller
@RequestMapping("/picture")
@UserLoginToken
public class PictureController {

    @Autowired
    FileUploadService fileUploadService;
    private static Logger logger = LoggerFactory.getLogger(PictureController.class);


    /**
     * 上传文件
     *
     * @param fileupload
     * @param uploadDirection aliyun:阿里云oss local:本地服务器 qiniu:七牛云
     * @param bucketName      空间名称
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult upload(MultipartFile fileupload, String uploadDirection,
                             String bucketName) {
        JsonResult jsonResult = fileUploadService.uploadFile(fileupload, uploadDirection, bucketName);
        logger.debug(jsonResult.toString());
        return jsonResult;
    }


    @RequestMapping(value = "/upload2", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult upload2() {
        return JsonResult.build(ResultCode.SUCCESS);
    }
}
