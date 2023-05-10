package com.nf.mvc;

import com.nf.Reflection;
import com.nf.mvc.annotation.RequestParam;
import com.nf.mvc.annotation.ValueConstants;
import com.nf.mvc.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class MethodParameter {
    private final Method method;

    private Parameter parameter;
    private String paramName;
    private int parameterIndex;
    private Class<?> containingClass;

    public MethodParameter(Method method, int parameterIndex, String paramName) {
        this(method, parameterIndex, paramName, method.getDeclaringClass());
    }

    public MethodParameter(Method method, int parameterIndex, String paramName, Class<?> containingClass) {
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
        // ��������ע�⣬����ֻ����������Ĭ��ֵ��û������value
        if(getParameter().isAnnotationPresent(RequestParam.class) ){
            String value = getParameter().getDeclaredAnnotation(RequestParam.class).value();
            // û������value��value�ͻᱣ��Ĭ��ֵ�����ֵ���ǲ����ã���Ȼ�÷����Ĳ�������javassist����������
            // �������ע�⣬����������value�����ǲŲ���value���Ե�ֵ��Ϊ������
            if (value.equals(ValueConstants.DEFAULT_NONE) == false) {
                this.paramName = value;
            }
        }
        return this.paramName;
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

    public boolean isArray() {
        return getParamType().isArray();
    }

    public Object getDefaultValue() {
        if ( getParameter().isAnnotationPresent(RequestParam.class)) {
            String defaultValue = getParameter().getDeclaredAnnotation(RequestParam.class).defaultValue();
            //�û�����ֻ������RequestParam�����˲������֣�û������Ĭ��ֵ
            if (!defaultValue.equals(ValueConstants.DEFAULT_NONE)) {
                return defaultValue;
            }
        }
        return null;
    }

    public Class<?> getComponentType() {
        if (isArray()) {
            return getParamType().getComponentType();
        }
        throw new IllegalStateException("�������飬�޷���ȡ�������");
    }

    public Class<?> getFirstActualTypeArgument() {
        return getActualArguments()[0];
    }

    public boolean isList() {
        return Reflection.isAssignable(List.class, getParamType());
    }

    /**
     * �˷�����������ȡ�������Ͳ���������ʵ�ε�
     * @return
     */
    public  Class[] getActualArguments() {
        return Reflection.getActualArgument(getParameter());
    }

    @Override
    public boolean equals(Object other) {
        //1.�Լ���������ͬһ����==��������ô�϶����
        if (this == other) {
            return true;
        }
        //2.���˸��Ҹ�������ͬһ�����͡����Ծ�һ�������
        if (!(other instanceof MethodParameter)) {
            return false;
        }
        //3.���뵽�������ζ�ž�����ͬһ�����󣬵�������һ����
        //����λ��һ�������ڷ���һ����������һ��
        MethodParameter otherParam = (MethodParameter) other;
        return (getContainingClass() == otherParam.getContainingClass() &&
                this.parameterIndex == otherParam.parameterIndex &&
                this.getMethod().equals(otherParam.getMethod()));
    }

    @Override
    public int hashCode() {
        return (31 * this.getMethod().hashCode() + this.parameterIndex);
    }
}
