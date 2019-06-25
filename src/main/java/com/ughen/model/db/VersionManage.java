package com.ughen.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import lombok.Data;

/**
 * 版本管理实体
 *
 * @Author feife
 */
@Data
public class VersionManage {

    /**
     * 版本号
     */
    private String versionNum;
    /**
     * 对应版本类型 ios   android
     */
    private String versionSource;
    /**
     * 是否需要强制更新 0-否，1-是
     */
    private int isForceUpdate;
    /**
     * 版本更新地址
     */
    private String versionLinkUrl;
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    private String welcomeImg;
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    private String welcomeTime;

}
