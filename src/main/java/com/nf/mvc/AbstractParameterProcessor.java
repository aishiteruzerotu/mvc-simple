package com.nf.mvc;

import com.nf.mvc.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public abstract class AbstractParameterProcessor<T> implements ParameterProcessor {

    protected Parameter parameter;

    protected Class<?> parameterType;

    @Override
    public boolean supports(Parameter parameter) {
        this.parameter = parameter;
        this.parameterType = parameter.getType();
        return parameter.getType().equals(getType()) || parameter.getType().equals(this.getArrayType())
                || this.basicType();
    }

    protected abstract Class<? extends T> getType();

    protected abstract Class<? extends T[]> getArrayType();

    protected abstract boolean basicType();

    @Override
    public Object processor(Handler handler, HttpServletRequest req) {
        String key = getKey(handler);
        if (this.parameterType.isArray()) {
            String[] strings = req.getParameterValues(key);
            return this.getConverts(strings);
        } else {
            return getConvert(req.getParameter(key));
        }
    }

    protected String getKey(Handler handler) {
        if (parameter.isAnnotationPresent(RequestParam.class)) {
            return parameter.getAnnotation(RequestParam.class).value();
        } else {
            return handler.getParamName(parameter);
        }
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
