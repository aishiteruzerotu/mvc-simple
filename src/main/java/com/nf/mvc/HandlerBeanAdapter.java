package com.nf.mvc;

import java.lang.reflect.Method;

public class HandlerBeanAdapter {
    Class<?> clz;
    Method method;

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
