package com.nf.mvc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public abstract class Handler {
    protected Class<?> clz;
    protected Method method;
    protected Parameter[] parameters;
    protected Map<Parameter,String> parameterNameMapping = new HashMap<>();

    public Handler() {
    }

    protected abstract void init();

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
        this.init();
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
        this.init();
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
        this.init();
    }

    public String getParamName(Parameter parameter){
        return this.parameterNameMapping.get(parameter);
    }
}
