package com.ughen.exception;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2019-01-24
 * Time: 17:12
 */
public class ExcelException extends Exception {

    public ExcelException() {

    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);

    }

    public ExcelException(Throwable cause) {
        super(cause);
    }

    protected ExcelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }
}