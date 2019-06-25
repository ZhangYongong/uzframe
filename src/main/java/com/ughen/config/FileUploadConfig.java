package com.ughen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2019-01-21
 * Time: 15:39
 */
@Component
@ConfigurationProperties(prefix = "local.file")
public class FileUploadConfig {

    private String uploadPath;
    private String domain;

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
