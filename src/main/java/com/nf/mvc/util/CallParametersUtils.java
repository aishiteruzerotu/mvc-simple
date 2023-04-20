package com.nf.mvc.util;

import com.nf.mvc.Handler;
import com.nf.mvc.annotation.RequestParam;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CallParametersUtils {
    private CallParametersUtils(){}

    public static Object[] getObjects(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws ServletException, IOException {
        Parameter[] parameters = handler.getParameters();
        int parameterCount = parameters.length;

        if(parameterCount==0){
            return new Object[0];
        }

        Object[] objects = new Object[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            Parameter parameter = parameters[i];
            String key;
            Object value;
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                key = parameter.getAnnotation(RequestParam.class).value();
            } else {
                key = getName(handler,i);
            }
            value = req.getParameter(key);
            objects[i] = value;
        }
        //TODO: 类型转化的问题，暂时先不解决
        return objects;
    }

    private static String getName(Handler handler,int index){
        List<String> paramNames = getParamNames(handler);
        return paramNames.get(index);
    }

    private static List<String> getParamNames(Handler handler) {
        Parameter[] parameters = handler.getParameters();
        Class[] classes = new Class[parameters.length];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = parameters[i].getType();
        }
        return getParamNames(handler.getClz(), handler.getMethod().getName(), classes);
    }

    /**
     * 用于获取为被编译的声明名称
     * @param clazz:方法所在的类
     * @param methodName：方法的名字
     * @param paramTypes：方法的参数类型，以便支持重载
     * @return 方法各个参数的名字（依据参数位置顺序依次返回）
     */
    public static List<String> getParamNames(Class<?> clazz, String methodName, Class... paramTypes) {
        List<String> paramNames = new ArrayList<>();
        ClassPool pool = ClassPool.getDefault();
        try {
            CtClass ctClass = pool.getCtClass(clazz.getName());

            CtClass[] paramClasses = new CtClass[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                paramClasses[i] =pool.get(paramTypes[i].getName());
            }
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName,paramClasses);
            // 使用javassist的反射方法的参数名
            javassist.bytecode.MethodInfo methodInfo = ctMethod.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr != null) {
                int len = ctMethod.getParameterTypes().length;
                // 非静态的成员函数的第一个参数是this
                int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
                for (int i = 0; i < len; i++) {
                    paramNames.add(attr.variableName(i + pos));
                }

            }
            return paramNames;
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
