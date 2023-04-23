package com.nf.mvc.util.parameter;

import com.nf.mvc.util.ParameterProcessor;

import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class IntegerParameterProcessor implements ParameterProcessor {

    private Class<?> parameterType;

    @Override
    public boolean supports(Parameter parameter) {
        this.parameterType = parameter.getType();
        return parameter.getType().equals(Integer.TYPE) || parameter.getType().equals(Integer[].class)
                || parameter.getType().equals(int.class) || parameter.getType().equals(int[].class);
    }

    @Override
    public Object getObject(Object obj) {
        if (obj.getClass().isArray()) {
            int len = Array.getLength(obj);
            Object[] objects = new Object[len];
            for (int i = 0; i < objects.length; i++) {
                objects[i] = Array.get(obj, i);
            }

            String[] strings = Arrays.copyOf(objects, objects.length, String[].class);
            int[] ints = Arrays.stream(strings).mapToInt(Integer::parseInt).toArray();
            if (this.parameterType.equals(Integer[].class)){
                return Arrays.stream(ints).boxed().toArray(Integer[]::new);
            }
            return ints;
        } else {
            //TODO : 无法解决 int 类型与 Integer 兼顾的问题
            return Integer.valueOf(obj.toString());
        }
    }
}
