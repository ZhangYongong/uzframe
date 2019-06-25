package com.ughen.exception;


/**
 * 配置文件异常
 * Author:@Yonghong Zhang
 * Date: 14:15 2019/1/23
 */
public class ConfigException extends Exception {

    public ConfigException() {
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }

    public ConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
