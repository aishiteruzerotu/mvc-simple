package com.nf.mvc.exception.exceptions;

/**
 * 无法获取值异常
 */
public class NoObtainValueException extends RuntimeException{
    public NoObtainValueException() {
    }

    public NoObtainValueException(String message) {
        super(message);
    }

    public NoObtainValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoObtainValueException(Throwable cause) {
        super(cause);
    }

    public NoObtainValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
