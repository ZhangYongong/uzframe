package com.ughen.util.qiniuyun;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.ughen.config.QiNiuConfig;
import com.ughen.exception.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 七牛文件上传
 *
 * @author weirdor
 *         2017年06月29日14:52:20
 *         开发文档：https://developer.qiniu.com/
 */

@Component
public class QiNiuUtil {

    private static final Logger logger = LoggerFactory.getLogger(QiNiuUtil.class);

    private static QiNiuConfig qiNiuConfig;

    @Autowired
    public QiNiuUtil(QiNiuConfig qiNiuConfig) {
        QiNiuUtil.qiNiuConfig = qiNiuConfig;
    }


    public static String localUpload(String localFilePath, String key, String bucket) throws Exception {
        Configuration cfg = qiNiuConfig.getConfiguration();
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            String finalUrl = String.format("%s/%s", qiNiuConfig.getPath(), putRet.key);
            return finalUrl;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            throw new QiniuException(ex);
        }
    }

    /**
     * 上传到七牛云
     *
     * @param file
     * @param key  保存在空间中的名字，如果为空会使用文件的hash值为文件名
     * @return
     */
    public static String upload(InputStream file, String key, String bucket) throws QiniuException, ConfigException {
        Configuration cfg = qiNiuConfig.getConfiguration();
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(file, key, upToken, null, null);

                //解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
                String finalUrl = String.format("%s/%s", qiNiuConfig.getPath(), putRet.key);
                return finalUrl;
            } catch (QiniuException ex) {
                throw new QiniuException(ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除七牛空间图片方法
     *
     * @param bucket 你的七牛用户创建的空间名
     * @param key    七牛空间中文件名称
     */
    public static Boolean removeFile(String bucket, String key) throws QiniuException, ConfigException {
        //设置需要操作的账号的AK和SK
        Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
        Configuration cfg = qiNiuConfig.getConfiguration();
        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
            return true;
        } catch (QiniuException e) {
            //捕获异常信息
            throw new QiniuException(e);
        }
    }


    /**
     * 文件移动或重命名
     *
     * @param fromBucket
     * @param fromKey
     * @param toBucket
     * @param toKey
     * @return
     */
    public static Boolean moveFile(String fromBucket, String fromKey, String toBucket, String toKey) throws QiniuException {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());

        Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.move(fromBucket, fromKey, toBucket, toKey);
            return true;
        } catch (QiniuException ex) {
            //如果遇到异常，说明移动失败
            throw new QiniuException(ex);
        }
    }

    /**
     * @param bucket
     * @param delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
     * @return
     */
    public static List<String> fileInfoList(String bucket, String delimiter) {
        List<String> list = new ArrayList<>();
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                list.add(item.key);
                //                System.out.println(item.key);
                //                System.out.println(item.hash);
                //                System.out.println(item.fsize);
                //                System.out.println(item.mimeType);
                //                System.out.println(item.putTime);
                //                System.out.println(item.endUser);
            }
        }
        return list;
    }


    /**
     * 批量删除
     *
     * @param bucket
     * @param keys
     * @return List<String> 删除失败文件集合
     * @throws QiniuException
     */
    public static List<String> delFileList(String bucket, List<String> keys) throws QiniuException {
        List<String> list = new ArrayList<>();
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //单次批量请求的文件数量不得超过1000
            String[] keyList = new String[keys.size()];
            keyList = keys.toArray(keyList);

            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keyList[i];
                System.out.print(key + "\t");
                if (200 != status.code) {
                    list.add(key);
                }
            }
            return list;
        } catch (QiniuException ex) {
            throw new QiniuException(ex);
        }
    }

}



