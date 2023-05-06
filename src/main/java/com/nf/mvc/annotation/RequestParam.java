package com.nf.mvc.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数注解，用于赋值名称以及默认值
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    //名称
    String value() default ValueConstants.DEFAULT_NONE;

    //默认值
    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
