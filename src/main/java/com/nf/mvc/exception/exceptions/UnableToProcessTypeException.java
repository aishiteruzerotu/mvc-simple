package com.nf.mvc.exception.exceptions;

/**
 * 无法转化类型异常
 */
public class UnableToProcessTypeException extends RuntimeException{
    public UnableToProcessTypeException() {
    }

    public UnableToProcessTypeException(String message) {
        super(message);
    }

    public UnableToProcessTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToProcessTypeException(Throwable cause) {
        super(cause);
    }

    public UnableToProcessTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
