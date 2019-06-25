package com.ughen.constants;

/**
 * 返回給前端的編碼統一寫在這個類裏,CODE整理後與前端溝通,返回給客戶的提示由前端用中文返回
 */
public class Constants {

    /**
     * 用戶登錄成功操作碼
     */
    public static final String CODE_UZ_FRAME_TYPE = "1000";
    public static final String MSG_UZ_FRAME_TYPE = "login success";

    public static final String LOGIN_DEFAULT = "default";
    public static final String LOGIN_THIRD_APP = "third-app";
    /**
     * 第三方登录
     */
    public static final String LOGIN_SINA = "sina";
    public static final String LOGIN_SINAWEIBO = "新浪微博";
    public static final String LOGIN_ALI = "ali";
    public static final String LOGIN_ALIPAY = "支付宝";
    public static final String LOGIN_QQ = "qq";

    public static final String APP = "app";
    public static final String YES = "yes";
    public static final String NO = "no";


    public static final String BASE64 = "base64";
    public static final String AES = "aes";

    /**
     * 验证码的sessionKey
     */
    public static final String VERIFY_SESSION_KEY = "checkCode";
    /**
     * 过期
     */
    public static final String OVERDUE = "overdue";
    /**
     * 盐
     */
    public static final String SALT = "uz_test";

    public static final Integer ALI_SMS_CODE_LENGTH = 6;
    public static final long SMS_CODE_EXPIRE_TIME = 300L;
    public static final long LOGIN_EXPIRE_TIME = 1800L;
    public static final long THIRD_USER_INFO_EXPIRE_TIME = 3600L;
    public static final long USER_ROLE_EXPIRE_TIME = 7200L;


    /**
     * 已关闭资源redis缓存key
     */
    public static final String CLOSE_RESOURCES_KEY = "resources:close";
}
