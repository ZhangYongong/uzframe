package com.ughen.model.db;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import lombok.Data;

/**
 * @author feifei
 */
@Data
public class ThirdInfo {

    @JsonSerialize(include = Inclusion.NON_EMPTY)
    private String source;
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    private String userId;
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    private String uid;
    /**
     * 登录设备 PC  WAP (可以多个登录) APP（只能登录一个设备）
     */
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    private String deviceNo;
    /**
     * qq login need it
     */
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    private String accessToken;
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    private String code;

    private String bindThird;
    private String msg;
    private String register;
}