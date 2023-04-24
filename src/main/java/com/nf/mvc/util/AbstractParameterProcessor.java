package com.nf.mvc.util;

import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public abstract class AbstractParameterProcessor<T>  implements ParameterProcessor {

    protected Class<?> parameterType;

    @Override
    public boolean supports(Parameter parameter) {
        this.parameterType = parameter.getType();
        return parameter.getType().equals(getType()) || parameter.getType().equals(this.getArrayType())
                || this.basicType();
    }

    protected abstract Class<? extends T> getType();

    protected abstract Class<? extends T[]> getArrayType();

    protected abstract boolean basicType();

    @Override
    public Object convert(Object obj) {
        if (obj.getClass().isArray()) {
            int len = Array.getLength(obj);
            Object[] objects = new Object[len];
            for (int i = 0; i < objects.length; i++) {
                objects[i] = Array.get(obj, i);
            }
            String[] strings = Arrays.copyOf(objects, objects.length, String[].class);
            return this.getConverts(strings);
        } else {
            return getConvert(obj);
        }
    }

    protected abstract Object getConvert(Object obj);

    protected abstract Object getConverts(String[] strings);

    protected T[] toArr(T[] arr,String[] strings){
        for (int i = 0; i < strings.length; i++) {
            arr[i] = getT(strings[i]);
        }
        return arr;
    }

    /**
     * 只给需要的子类重写
     * @param str strings
     * @return 指定的对象
     */
    protected T getT(String str){
        return null;
    }
}
