package com.nf.mvc.util;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;

import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {
    public static Object newInstance(Class<?> scanedClass) {
        try {
            return scanedClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            return null;
        }
    }

    /**
     * 用于获取为被编译的声明名称
     * @param clazz:方法所在的类
     * @param methodName：方法的名字
     * @param paramTypes：方法的参数类型，以便支持重载
     * @return 方法各个参数的名字（依据参数位置顺序依次返回）
     */
    public static List<String> getParamNames(Class<?> clazz, String methodName, Class... paramTypes) {
        if(paramTypes.length==0){
            return null;
        }
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
            throw new RuntimeException("无法获取到参数的名称",e);
        }
    }
}
