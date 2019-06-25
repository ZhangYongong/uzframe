package com.ughen.util.aliyun.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.ughen.config.AliOSSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 阿里云OSS 管理文件
 * Author:@Yonghong Zhang
 * Date: 11:36 2019/1/22
 */
@Component
public class FileManagement {

    private volatile static OSSClient instance;
    private static AliOSSConfig aliOSSConfig;

    @Autowired
    public FileManagement(AliOSSConfig aliOSSConfig) {
        FileManagement.aliOSSConfig = aliOSSConfig;
    }


    /**
     * @return OSS工具类实例
     */
    private static OSSClient getOSSClient() {
        if (instance == null) {
            synchronized (FileManagement.class) {
                if (instance == null) {
                    instance = new OSSClient(aliOSSConfig.getEndpoint(), aliOSSConfig.getAccessKeyId(), aliOSSConfig.getAccessKeySecret());
                }
            }
        }
        return instance;
    }


    /**
     * 设置文件访问权限
     *
     * @param bucketName
     * @param fileName
     * @throws OSSException
     * @throws ClientException
     */
    public void setObjectAcl(String bucketName, String fileName) throws OSSException, ClientException {
        OSSClient ossClient = getOSSClient();
        // 设置文件的访问权限为公共读
        try {
            ossClient.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);
        } catch (OSSException ex) {
            throw new OSSException();
        } catch (ClientException ex) {
            throw new ClientException();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName
     * @param fileName
     * @throws OSSException
     * @throws ClientException
     */
    public void delObject(String bucketName, String fileName) throws OSSException, ClientException {
        OSSClient ossClient = getOSSClient();
        try {
            ossClient.deleteObject(bucketName, fileName);
        } catch (OSSException ex) {
            throw new OSSException();
        } catch (ClientException ex) {
            throw new ClientException();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }

    /**
     * 删除多个文件(最多100个)
     *
     * @param bucketName
     * @param lsit
     * @return 成功删除的列表
     * @throws OSSException
     * @throws ClientException
     */
    public List<String> delObjectLsit(String bucketName, List<String> lsit) throws OSSException, ClientException {
        OSSClient ossClient = getOSSClient();
        List<String> deletedObjects = new ArrayList<>();
        try {
            DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(lsit));
            deletedObjects = deleteObjectsResult.getDeletedObjects();
        } catch (OSSException ex) {
            throw new OSSException();
        } catch (ClientException ex) {
            throw new ClientException();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return lsit;
    }
}
