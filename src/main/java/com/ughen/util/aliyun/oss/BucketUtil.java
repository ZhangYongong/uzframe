package com.ughen.util.aliyun.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.BucketList;
import com.aliyun.oss.model.ListBucketsRequest;
import com.ughen.config.AliOSSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 阿里云存储空间管理
 * Author:@Yonghong Zhang
 * Date: 10:48 2019/1/22
 */
@Component
public class BucketUtil {

    private volatile static OSSClient instance;
    private static AliOSSConfig aliOSSConfig;


    @Autowired
    public BucketUtil(AliOSSConfig aliOSSConfig) {
        BucketUtil.aliOSSConfig = aliOSSConfig;
    }

    /**
     * @return OSS工具类实例
     */
    private static OSSClient getOSSClient() {
        if (instance == null) {
            synchronized (BucketUtil.class) {
                if (instance == null) {
                    instance = new OSSClient(aliOSSConfig.getEndpoint(), aliOSSConfig.getAccessKeyId(), aliOSSConfig.getAccessKeySecret());
                }
            }
        }
        return instance;
    }

    /**
     * 创建存储空间
     *
     * @param bucketName 存储空间名称
     *                   只能包括小写字母、数字和短横线（-）。
     *                   必须以小写字母或者数字开头和结尾。
     *                   长度必须在3–63字节之间。
     * @throws OSSException
     * @throws ClientException
     */
    public static void createBucket(String bucketName) throws OSSException, ClientException {
        // 创建OSSClient实例。
        OSSClient ossClient = getOSSClient();
        try {
            boolean exists = ossClient.doesBucketExist(bucketName);
            if (exists) {
                // 创建存储空间。
                // 新建存储空间默认为标准存储类型，私有权限。
                ossClient.createBucket(bucketName);
            }
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
     * 列举所有的存储空间
     *
     * @return
     * @throws OSSException
     * @throws ClientException
     */
    public static List<String> getBucketList() throws OSSException, ClientException {
        OSSClient ossClient = getOSSClient();

        List<String> list = new ArrayList<String>();
        try {
            // 列举存储空间。
            List<Bucket> buckets = ossClient.listBuckets();
            for (Bucket bucket : buckets) {
                list.add(bucket.getName());
            }
        } catch (OSSException ex) {
            throw new OSSException();
        } catch (ClientException ex) {
            throw new ClientException();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return list;
    }

    /**
     * 列举包含指定前缀（prefix）的存储空间：
     *
     * @param prefix
     * @return
     * @throws OSSException
     * @throws ClientException
     */
    public static List<String> getBucketListByName(String prefix) throws OSSException, ClientException {
        OSSClient ossClient = getOSSClient();
        List<String> list = new ArrayList<String>();
        try {
            ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
            // 列举指定前缀的存储空间。
            listBucketsRequest.setPrefix(prefix);
            BucketList bucketList = ossClient.listBuckets(listBucketsRequest);
            for (Bucket bucket : bucketList.getBucketList()) {
                list.add(bucket.getName());
            }
        } catch (OSSException ex) {
            throw new OSSException();
        } catch (ClientException ex) {
            throw new ClientException();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return list;
    }


    /**
     * 获取存储空间的信息
     *
     * @param bucketName
     * @return info.getBucket().getLocation()--获取地域
     * info.getBucket().getCreationDate() -- 获取创建日期
     * info.getBucket().getOwner() -- 获取拥有者信息
     * info.getGrants() -- 获取权限信息
     * @throws OSSException
     * @throws ClientException
     */
    public static BucketInfo getBucketInfo(String bucketName) throws OSSException, ClientException {
        OSSClient ossClient = getOSSClient();
        // 存储空间的信息包括地域（Region或Location）、创建日期（CreationDate）、拥有者（Owner）、权限（Grants）等。
        BucketInfo info;
        try {
            info = ossClient.getBucketInfo(bucketName);
        } catch (OSSException ex) {
            throw new OSSException();
        } catch (ClientException ex) {
            throw new ClientException();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return info;
    }


    /**
     * 删除存储空间
     *
     * @param bucketName
     * @throws OSSException
     * @throws ClientException
     */
    public static void delBucket(String bucketName) throws OSSException, ClientException {
        OSSClient ossClient = getOSSClient();
        // 存储空间的信息包括地域（Region或Location）、创建日期（CreationDate）、拥有者（Owner）、权限（Grants）等。
        try {
            ossClient.deleteBucket(bucketName);
        } catch (OSSException ex) {
            throw new OSSException();
        } catch (ClientException ex) {
            throw new ClientException();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }
}
