package com.ughen.constants;

public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(1000, "成功"),
    ERROR(1001, "失败"),
    NETWORKEXCEPTION(1002, "网络异常，请稍后重试"),

    SMS_TYPPER_ERROR(1003, "短信类型错误"),
    SMS_BUSINESS_LIMIT_CONTROL(1004, "短信发送频繁，请稍后重试"),
    SMS_OTHER_ERROER(1005, "其它错误"),
    SMS_CODE_RESTART(1006, "请重新获取验证码"),
    SMS_CODE_OVERDUE(1007, " 验证码已过期，请重新获取"),

    NO_PERMISSION(1008, "无权限访问"),
    FILE_NULL(1009, "上传文件为空"),
    FILE_ERROR(1010, "上传文件为空"),


    EXIST_USER(1100, "用户名已存在"),
    INSTER_USER_ERROR(1101, "新增用户失败"),

    VALIDATION_ERROR(1110, "账号或密码错误"),
    NOT_SAME_PASSWORD(1111, "密码不一样"),

    THIRD_CODE_FAILURE(1112, "CODE已过期"),
    NO_TESTUSER(1113, "无此测试号"),
    TOKEN_EXPIRE(1114, "TOKEN已过期"),
    THIRD_SOURCE_ERROR(1115,"第三方登录类型错误"),


    VERIFY_ERROR(1120, "验证码有误"),
    VERIFY_OVERDUE(1121, "验证码已过期"),
    MISSING_PARAM(1130, "缺少如下参数或部分参数"),

    VERSION_NOT_EXIST(1140,"版本不存在"),
    VERSION_IS_NEW(1141,"此版本为最新版本"),
    CLOSE_RESOURCES(1142, "资源已关闭"),

//    VERSION_NOT_EXIST(1140,"版本不存在"),
    ;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
