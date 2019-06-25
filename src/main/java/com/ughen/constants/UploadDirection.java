package com.ughen.constants;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2019-01-24
 * Time: 14:16
 */
public enum UploadDirection {
    /**
     * 阿里云
     */
    ALIYUN("aliyun"),
    /**
     * 本地
     */
    LOCAL("local"),
    /**
     * 七牛云
     */
    QINIU("qiniu");


    private String value;

    UploadDirection(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
