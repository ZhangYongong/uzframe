package com.ughen.exception;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2019-01-29
 * Time: 18:20
 */
public class TokenException extends RuntimeException {
    public TokenException() {

    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(Throwable cause) {
        super(cause);

    }

    protected TokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
