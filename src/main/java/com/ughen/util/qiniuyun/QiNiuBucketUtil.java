package com.ughen.util.qiniuyun;

import com.qiniu.util.Auth;
import com.ughen.config.QiNiuConfig;
import com.ughen.constants.QiNiuInterface;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 阿里云存储空间管理
 * Author:@Yonghong Zhang
 * Date: 10:48 2019/1/22
 */
@Component
public class QiNiuBucketUtil {


    private static QiNiuConfig qiNiuConfig;

    @Autowired
    public QiNiuBucketUtil(QiNiuConfig qiNiuConfig) {
        QiNiuBucketUtil.qiNiuConfig = qiNiuConfig;
    }


    /**
     * 列举一个账号的所有空间
     */
    public static List<String> bucketLsit() throws IOException {
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "8888");
//        System.setProperty("https.proxyPort", "8888");
        //        Auth auth = Auth.create("R5P9jD5_LpU1iC3NmYG_Sh49L-V9NWI4O2vvCCPK", "OA0BnqWE7kUbXrDLaUqcj-LkmbrCNID_JNQUMmC3");
        Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
        List<String> list = new ArrayList<>();
        QiNiuInterface qiNiuInterface = QiNiuInterface.BUCKETS;
        String path = qiNiuInterface.getValue() + "\n";
        String accessToken = auth.sign(path);
        String url = qiNiuInterface.getHost() + qiNiuInterface.getValue();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "QBox " + accessToken).build();
        Response re = null;
        try {
            re = client.newCall(request).execute();
            if (re.isSuccessful() && 200 == re.code()) {
                //获取返回值 字符串格式数组
                String result = re.body().string();
                String[] split = result.substring(1, result.length() - 1).split(",");
                list = Arrays.asList(split);
                return list;
            } else {
                return list;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    /**
     * 获取一个空间绑定的域名列表
     */
    public static List<String> domainList(String bucketName) throws IOException {
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "8888");
//        System.setProperty("https.proxyPort", "8888");
//        Auth auth = Auth.create("R5P9jD5_LpU1iC3NmYG_Sh49L-V9NWI4O2vvCCPK", "OA0BnqWE7kUbXrDLaUqcj-LkmbrCNID_JNQUMmC3");
        Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
        QiNiuInterface qiNiuInterface = QiNiuInterface.DOMAINLIST;
        String path = qiNiuInterface.getValue() + "?tbl+" + bucketName + "\n";
        String accessToken = auth.sign(path);
        String url = qiNiuInterface.getHost() + qiNiuInterface.getValue() + "?tbl+" + bucketName;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "QBox " + accessToken).build();
        Response re = null;
        try {
            List<String> list = new ArrayList<>();
            re = client.newCall(request).execute();
            if (re.isSuccessful() && 200 == re.code()) {
                //获取返回值 字符串格式数组
                String result = re.body().string();
                String[] split = result.substring(1, result.length() - 1).split(",");
                list = Arrays.asList(split);
                return list;
            } else {
                return list;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    /**
     * 创建空间
     *
     * @param bucketName 空间名称仅支持字母、短划线-、下划线_、数字的组合
     * @return
     * @throws IOException
     */
    public static Boolean createBucket(String bucketName) throws IOException {
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "8888");
//        System.setProperty("https.proxyPort", "8888");
//        Auth auth = Auth.create("R5P9jD5_LpU1iC3NmYG_Sh49L-V9NWI4O2vvCCPK", "OA0BnqWE7kUbXrDLaUqcj-LkmbrCNID_JNQUMmC3");
        Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
        QiNiuInterface qiNiuInterface = QiNiuInterface.MKBUCKETV2;
        String path = qiNiuInterface.getValue() + encode(bucketName.getBytes()) + "/public/z0\n";
        String accessToken = auth.sign(path);
        String url = qiNiuInterface.getHost() + qiNiuInterface.getValue() + encode(bucketName.getBytes()) + "/public/z0";
        System.out.println(url);
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "";
        Request request = new Request.Builder().url(url).post(RequestBody.create(mediaType, requestBody))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "QBox " + accessToken).build();
        Response re = null;
        try {
            re = client.newCall(request).execute();
            if (re.isSuccessful() && 200 == re.code()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    /**
     * 删除空间
     */
    public static Boolean drop(String bucketName) throws IOException {
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "8888");
//        System.setProperty("https.proxyPort", "8888");
//        Auth auth = Auth.create("R5P9jD5_LpU1iC3NmYG_Sh49L-V9NWI4O2vvCCPK", "OA0BnqWE7kUbXrDLaUqcj-LkmbrCNID_JNQUMmC3");
        Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
        QiNiuInterface qiNiuInterface = QiNiuInterface.DROP;
        String path = qiNiuInterface.getValue() + bucketName + "\n";
        String accessToken = auth.sign(path);
        String url = qiNiuInterface.getHost() + qiNiuInterface.getValue() + bucketName;
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "";
        Request request = new Request.Builder().url(url).post(RequestBody.create(mediaType, requestBody))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "QBox " + accessToken).build();
        Response re = null;
        try {
            re = client.newCall(request).execute();
            if (re.isSuccessful() && 200 == re.code()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    /**
     * 编码
     *
     * @param bstr
     * @return String
     */
    public static String encode(byte[] bstr) {
        return new sun.misc.BASE64Encoder().encode(bstr);
    }


}
