package com.nf.mvc;

import com.nf.Reflection;
import com.nf.mvc.annotation.RequestParam;
import com.nf.mvc.annotation.ValueConstants;

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
        // 参数上有注解，可能只是用来设置默认值，没有设置value
        if(getParameter().isAnnotationPresent(RequestParam.class) ){
            String value = getParameter().getDeclaredAnnotation(RequestParam.class).value();
            // 没有设置value，value就会保留默认值，这个值我们不采用，仍然用方法的参数名（javassist解析出来的
            // 如果你有注解，并且设置了value，我们才采用value属性的值作为参数名
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
            //用户可能只是利用RequestParam设置了参数名字，没有设置默认值
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
        throw new IllegalStateException("不是数组，无法获取组件类型");
    }

    public Class<?> getFirstActualTypeArgument() {
        return getActualArguments()[0];
    }

    public boolean isList() {
        return Reflection.isAssignable(List.class, getParamType());
    }

    /**
     * 此方法是用来获取方法泛型参数的类型实参的
     * @return
     */
    public  Class[] getActualArguments() {
        return Reflection.getActualArgument(getParameter());
    }

    @Override
    public boolean equals(Object other) {
        //1.自己与其它是同一个（==）对象，那么肯定相等
        if (this == other) {
            return true;
        }
        //2.别人跟我根本不是同一个类型。所以就一定不相等
        if (!(other instanceof MethodParameter)) {
            return false;
        }
        //3.代码到这里，就意味着绝不是同一个对象，但类型是一样的
        //参数位置一样，所在方法一样，所在类一样
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
