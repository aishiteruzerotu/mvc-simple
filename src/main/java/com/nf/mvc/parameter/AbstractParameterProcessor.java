package com.nf.mvc.parameter;

import com.nf.mvc.MethodParameter;
import com.nf.mvc.ParameterProcessor;
import com.nf.mvc.annotation.RequestParam;
import com.nf.mvc.annotation.ValueConstants;

/**
 * 该抽象类为子类提供了一个获取 key 值的方法
 */
public abstract class AbstractParameterProcessor implements ParameterProcessor {
    /**
     * 获取 key
     * @param methodParameter 封装的 Parameter 对象
     * @return String 类型的 key 值
     */
    protected String getKey(MethodParameter methodParameter) {
        String key = methodParameter.getParamName();
        if (methodParameter.getParameter().isAnnotationPresent(RequestParam.class)) {
            String value = methodParameter.getParameter().getAnnotation(RequestParam.class).value();
            if (!value.equals(ValueConstants.DEFAULT_NONE)) {
                key = value;
            }
        }
        return key;
    }
}
