package com.nf.mvc;

import com.nf.mvc.annotation.RequestParam;
import com.nf.mvc.annotation.ValueConstants;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public abstract class AbstractParameterProcessor<T> implements ParameterProcessor {

    protected Parameter parameter;

    protected Class<?> parameterType;

    @Override
    public boolean supports(Parameter parameter) {
        this.parameter = parameter;
        this.parameterType = parameter.getType();
        return this.isType(parameter);
    }

    protected boolean isType(Parameter parameter) {
        return parameter.getType().equals(getType()) || parameter.getType().equals(this.getArrayType())
                || this.basicType();
    }

    protected Class<? extends T> getType(){return null;}

    protected Class<? extends T[]> getArrayType(){return null;}

    protected boolean basicType(){return false;}

    @Override
    public Object processor(Handler handler, HttpServletRequest req) {
        String key = getKey(handler);
        if (this.parameterType.isArray()) {
            String[] strings = req.getParameterValues(key);
            return this.getConverts(strings);
        } else {
            return this.getConvert(this.getValue(req, key));
        }
    }

    protected String getValue(HttpServletRequest req, String key) {
        String value = req.getParameter(key);
        if (value==null&&this.parameterType.isPrimitive()){
            throw new IllegalArgumentException("不能把null给简单类型");
        }
        if (value==null&&this.parameter.isAnnotationPresent(RequestParam.class)){
            String defaultValue = this.parameter.getDeclaredAnnotation(RequestParam.class).defaultValue();
            if (!defaultValue.equals(ValueConstants.DEFAULT_NONE)) {
                value = defaultValue;
            }
        }
        return value;
    }

    protected String getKey(Handler handler) {
        String key = handler.getParamName(this.parameter);
        if (this.parameter.isAnnotationPresent(RequestParam.class)) {
            String value = this.parameter.getAnnotation(RequestParam.class).value();
            if (!value.equals(ValueConstants.DEFAULT_NONE)) {
                key = value;
            }
        }
        return key;
    }

    protected abstract Object getConvert(String str);

    protected abstract Object getConverts(String[] strings);

    protected T[] toArr(T[] arr, String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            arr[i] = getT(strings[i]);
        }
        return arr;
    }

    /**
     * 只给需要的子类重写
     *
     * @param str strings
     * @return 指定的对象
     */
    protected T getT(String str) {
        return null;
    }
}
