package com.ughen.util.aliyun.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.ughen.config.AliOSSConfig;
import com.ughen.constants.FileType;
import com.ughen.util.FileUtil;
import com.ughen.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * 阿里云OSS 上传
 * Author:@Yonghong Zhang
 * Date: 14:59 2019/1/22
 */
@Component
public class AliOssUtil {

    private static Logger logger = LoggerFactory.getLogger(AliOssUtil.class);

    private volatile static OSSClient instance;

    private static AliOSSConfig aliOSSConfig;

    @Autowired
    public AliOssUtil(AliOSSConfig aliOSSConfig) {
        AliOssUtil.aliOSSConfig = aliOSSConfig;
    }

    /**
     * @return OSS工具类实例
     */
    private static OSSClient getOSSClient() {
        if (instance == null) {
            synchronized (AliOssUtil.class) {
                if (instance == null) {
                    instance = new OSSClient(aliOSSConfig.getEndpoint(), aliOSSConfig.getAccessKeyId(), aliOSSConfig.getAccessKeySecret());
                }
            }
        }
        return instance;
    }


    /**
     * 把文件保存到阿里云OSS，返回路径保存到数据库
     *
     * @param fileupload
     * @param bucketName 文件桶
     * @return String 文件的访问地址
     * @throws OSSException
     * @throws ClientException
     * @throws IOException
     */
    public static String uploadOSS(MultipartFile fileupload, String bucketName) throws ClientException, IOException, ParseException {
        //判断 文件桶 是否存在 不存在则新建
        BucketUtil.createBucket(bucketName);
        String filename = fileupload.getOriginalFilename();
        // 该桶中的文件key fileDir + 20180322010634.jpg
        String dateString = FileUtil.renameToUUID(filename);
        // 创建上传文件的元信息，可以通过文件元信息设置HTTP header。
        ObjectMetadata meta = new ObjectMetadata();
        String md5 = BinaryUtil.toBase64String(BinaryUtil.calculateMd5(fileupload.getBytes()));
        // 开启文件内容MD5校验。开启后OSS会把您提供的MD5与文件的MD5比较，不一致则抛出异常。
        meta.setContentMD5(md5);
        // 指定上传的内容类型。内容类型决定浏览器将以什么形式、什么编码读取文件。如果没有指定则根据文件的扩展名生成，如果没有扩展名则为默认值application/octet-stream。
        meta.setContentType(getContentType(filename));
        // 设置内容被下载时的名称。
        meta.setContentDisposition("attachment; filename=\"" + filename + "\"");
        // 设置上传文件的长度。如超过此长度，则会被截断，为设置的长度。如不足，则为上传文件的实际长度。
        meta.setContentLength(fileupload.getSize());
        // 设置内容被下载时网页的缓存行为。
        meta.setCacheControl("Download Action");
        // 设置缓存过期时间，格式是格林威治时间（GMT）。设置过期时间为100年
        meta.setExpirationTime(new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 100));
        // 设置内容被下载时的编码格式。
        meta.setContentEncoding("utf-8");
        // 设置header。
        meta.setHeader(bucketName, dateString);
        OSSClient ossClient = getOSSClient();
        // 上传文件。
        try {
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, dateString, new ByteArrayInputStream(fileupload.getBytes()), meta);
            logger.debug("上传成功");
            return putObjectResult.getETag();
        } catch (OSSException ex) {
            logger.debug("上传失败");
            throw new OSSException();
        } catch (ClientException ex) {
            logger.debug("上传失败", ex);
            throw new ClientException();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }


    public static String getContentType(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String contentType = "";
        for (FileType type : FileType.values()) {
            if (type.getSuffix().equalsIgnoreCase(fileExtension)) {
                contentType = type.getContentType();
                break;
            }
        }
        if (StringUtil.isEmpty(contentType)) {
            contentType = "";
        }
        return contentType;
    }


    /**
     * 下载到本地文件
     *
     * @param bucketName
     * @param localFile
     * @param objectName
     * @throws ClientException
     * @throws IOException
     * @throws ParseException
     */
    public static void download(String bucketName, String localFile, String objectName) throws ClientException, IOException {
        OSSClient ossClient = getOSSClient();
        try {
            //判断文件是否存在
            if (ossClient.doesObjectExist(bucketName, objectName)) {
                //下载文件
                ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(localFile));
                logger.debug("下载成功");
            }
        } catch (OSSException ex) {
            logger.debug("下载失败");
            throw new OSSException();
        } catch (ClientException ex) {
            logger.debug("下载失败",ex);
            throw new ClientException();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }

}
