package com.ughen.model.db;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import lombok.Data;

/**
 * @author feifei
 */
@Data
public class User {

    private Long userId;
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @JSONField(serialize = false)
    private String username;
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @JSONField(serialize = false)
    private String password;
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @JSONField(serialize = false)
    private String twoPassword;
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @JSONField(serialize = false)
    private String mobile;
    /**
     * 短信验证码
     */
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @JSONField(serialize = false)
    private String smsCode;
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @JSONField(serialize = false)
    private String mail;
    /**
     * 登录设备 PC  WAP (可以多个登录) APP（只能登录一个设备）
     */
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @JSONField(serialize = false)
    private String deviceNo;
    /**
     * 登录平台，测试uzTest,
     */
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @JSONField(serialize = false)
    private String platform;
    private String accessToken;
    /**
     * 登录方式   default 账号登录    短信验证码登录  sms
     */
    @JSONField(serialize = false)
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    private String loginType;
    private String nickname;
    /**
     * 第三方code
     */
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @JSONField(serialize = false)
    private String code;
    /**
     * 用户头像
     */
    private String headImgPath;
    /**
     * 是否绑定第三方 yes 是   no 否
     */
    @JsonSerialize(include = Inclusion.NON_EMPTY)
    @JSONField(serialize = false)
    private String bindThird;

    private String source;
}
