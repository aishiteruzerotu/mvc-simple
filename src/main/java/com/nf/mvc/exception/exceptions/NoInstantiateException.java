package com.nf.mvc.exception.exceptions;

public class NoInstantiateException extends RuntimeException{
    public NoInstantiateException() {
    }

    public NoInstantiateException(String message) {
        super(message);
    }

    public NoInstantiateException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoInstantiateException(Throwable cause) {
        super(cause);
    }

    public NoInstantiateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
