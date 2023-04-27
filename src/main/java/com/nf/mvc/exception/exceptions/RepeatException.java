package com.nf.mvc.exception.exceptions;

/**
 * Map key 重复异常
 */
public class RepeatException extends RuntimeException{
    public RepeatException() {
    }

    public RepeatException(String message) {
        super(message);
    }

    public RepeatException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatException(Throwable cause) {
        super(cause);
    }

    public RepeatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
