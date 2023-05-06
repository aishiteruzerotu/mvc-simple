package com.nf.mvc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter {
    private final Method method;

    private Parameter parameter;
    private final String paramName;
    private final int parameterIndex;
    private final Class<?> containingClass;

    public MethodParameter(Method method, int parameterIndex,String paramName) {
        this(method, parameterIndex, paramName,method.getDeclaringClass());
    }

    public MethodParameter(Method method, int parameterIndex, String paramName,Class<?> containingClass) {
        this.method = method;
        this.parameterIndex = parameterIndex;
        this.paramName = paramName;
        this.containingClass = containingClass;

    }

    public Class<?> getParamType() {
        return getParameter().getType();
    }

    public Parameter getParameter() {
        if (this.parameterIndex < 0) {
            throw new IllegalStateException("Cannot retrieve Parameter descriptor for method return type");
        }
        Parameter parameter = this.parameter;
        if (parameter == null) {
            parameter = method.getParameters()[this.parameterIndex];
            this.parameter = parameter;
        }
        return parameter;
    }

    public String getParamName() {
        return paramName;
    }

    public Method getMethod() {
        return method;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public Class<?> getContainingClass() {
        return containingClass;
    }
}
