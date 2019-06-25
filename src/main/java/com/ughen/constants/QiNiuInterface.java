package com.ughen.constants;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2019-01-23
 * Time: 18:03
 */
public enum QiNiuInterface {
    /**
     * 列举一个账号的所有空间
     */
    BUCKETS("/buckets", "http://rs.qbox.me"),
    /**
     * 创建存储空间，同时绑定一个七牛二级域名，用于访问资源
     */
    MKBUCKETV2("/mkbucketv2/", "http://rs.qiniu.com"),
    /**
     * 删除指定存储空间
     */
    DROP("/drop/", "http://rs.qbox.me"),
    /**
     * 获取一个空间绑定的域名列表
     */
    DOMAINLIST("/domain/list/", "http://api.qiniu.com");

    String value;
    String host;

    QiNiuInterface(String value, String host) {
        this.value = value;
        this.host = host;
    }


    public String getValue() {
        return value;
    }


    public String getHost() {
        return host;
    }
}
