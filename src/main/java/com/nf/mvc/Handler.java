package com.nf.mvc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public abstract class Handler {
    protected Class<?> clz;
    protected Method method;
    protected Parameter[] parameters;

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

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }
}
