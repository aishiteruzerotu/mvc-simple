package com.nf.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于获取类映射地址  一般是 /product/list 的 /product 部分
 * 也可以是 /user/product/list 的 /user/product 部分
 * 都是不包括 /list
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestController {
    String value() default "";
}
