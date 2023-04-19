package com.nf.mvc.util;

public class ReflectionUtils {
    public static Object newInstance(Class<?> scanedClass) {
        try {
            return scanedClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            return null;
        }
    }
}
