package com.nf.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应用于 HandlerInterceptor 的实现类
 * 用于实现拦截对应映射地址
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Interceptor {
    //拦截映射地址数组
    String[] values() default {};

    //拦截映射地址，当前赋值的映射优先级最高，如果未赋值则按数组进行判断
    String value() default ValueConstants.DEFAULT_NONE;
}
