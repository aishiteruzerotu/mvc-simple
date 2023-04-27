package com.nf.mvc.exception;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 标注该方法是处理全局异常的
 * 参数是异常类
 */
public @interface ExceptionHandler {
    Class<? extends Exception> value() ;
}
