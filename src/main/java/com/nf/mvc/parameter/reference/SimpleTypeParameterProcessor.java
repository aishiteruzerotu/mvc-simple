package com.nf.mvc.parameter.reference;

import com.nf.Convert;
import com.nf.Reflection;
import com.nf.mvc.Handler;
import com.nf.mvc.MethodParameter;
import com.nf.mvc.ParameterProcessor;
import com.nf.mvc.annotation.RequestParam;
import com.nf.mvc.annotation.ValueConstants;
import com.nf.mvc.exception.exceptions.NoAssignmentToPrimitiveIsNullException;
import com.nf.mvc.exception.exceptions.UnableToProcessTypeException;
import com.nf.mvc.parameter.AbstractParameterProcessor;
import com.nf.mvc.support.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

@Order(1)
public class SimpleTypeParameterProcessor extends AbstractParameterProcessor {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParamType();
        if (Reflection.isSimpleProperty(parameterType)) {
            return true;
        }
        if (Reflection.isAssignableToAny(parameterType, Set.class, List.class)) {
            // 只支持简单类型 List Set 集合
            ParameterizedType parameterizedType = (ParameterizedType) methodParameter.getParameter().getParameterizedType();
            Class<?> genericType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            return Reflection.isSimpleType(genericType);
        }
        return false;
    }

    @Override
    public Object processor(MethodParameter methodParameter, HttpServletRequest req) throws IOException, ServletException {

        //获取参数对象
        Parameter parameter = methodParameter.getParameter();

        Object value;
        String paramName = getKey(methodParameter);
        //获取参数类型
        Class<?> paramType = methodParameter.getParamType();
        //转换为数组、集合、或具体类型的值
        try {
            if (paramType.isArray()) {
                Class<?> componentType = paramType.getComponentType();
                value = Convert.toSimpleTypeArray(componentType, req.getParameterValues(paramName));
            } else if (List.class.isAssignableFrom(paramType) || Set.class.isAssignableFrom(paramType)) {
                //获取集合泛型参数类型
                ParameterizedType type = (ParameterizedType) parameter.getParameterizedType();
                Class<?> genericType = (Class<?>) type.getActualTypeArguments()[0];
                value = Convert.toSimpleCollection(paramType, genericType, req.getParameterValues(paramName));
            } else {
                //比如String name,简单类型解析要考虑RequestParam注解
                //如果出现转换异常？比如前端没有传递值，那么转成Integer这种类型就报异常
                //前端没有传值，那么getP就是null，赋给String这种是可以，但这个defaultValue要开始生效

                //toSimpleTypeValue方法的实现，是转换出了异常就返回null
                value = Convert.toSimpleTypeValue(paramType, req.getParameter(paramName));

                if (value == null && parameter.isAnnotationPresent(RequestParam.class)) {
                    String defaultValue = parameter.getDeclaredAnnotation(RequestParam.class).defaultValue();
                    if (!defaultValue.equals(ValueConstants.DEFAULT_NONE)) {
                        value = Convert.toSimpleTypeValue(paramType,defaultValue);
                    }
                }

                if (value == null && Reflection.isPrimitive(paramType)) {
                    throw new NoAssignmentToPrimitiveIsNullException("不能把null给简单类型");
                }

            }
        } catch (Exception e) {
            return new UnableToProcessTypeException("无法转换值异常",e);
        }
        return value;
    }
}
