package com.nf.mvc.util;

import com.nf.mvc.Handler;

import java.lang.reflect.Method;

public class HandlerInvokeUtils {
    private HandlerInvokeUtils(){}
    public static Object invoke(Handler handler,Object... args)  {
        Object instance = ReflectionUtils.newInstance(handler.getClz());
        Method method = handler.getMethod();
        Object obj = null;
        try {
            if (handler.getParameters().length==0){
                obj = method.invoke(instance);
            }else {
                obj = method.invoke(instance,args);
            }
        } catch (Exception e){
            System.out.println("无法运行");
        }
        return obj;
    }
}
