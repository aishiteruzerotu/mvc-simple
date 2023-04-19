package com.nf.mvc.util;

import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {
    public static Object newInstance(Class<?> scanedClass) {
        try {
            return scanedClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            return null;
        }
    }
}
