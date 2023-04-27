package com.nf.mvc.exception.exceptions;

/**
 * 无法调用异常
 */
public class UnableToExecuteException extends RuntimeException{
    public UnableToExecuteException() {
    }

    public UnableToExecuteException(String message) {
        super(message);
    }

    public UnableToExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToExecuteException(Throwable cause) {
        super(cause);
    }

    public UnableToExecuteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
