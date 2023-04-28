package com.nf.mvc.exception.exceptions;

/**
 * 不能把值赋值给简单类型
 */
public class NoAssignmentToPrimitiveIsNullException extends RuntimeException{
    public NoAssignmentToPrimitiveIsNullException() {
    }

    public NoAssignmentToPrimitiveIsNullException(String message) {
        super(message);
    }

    public NoAssignmentToPrimitiveIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAssignmentToPrimitiveIsNullException(Throwable cause) {
        super(cause);
    }

    public NoAssignmentToPrimitiveIsNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
