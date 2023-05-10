package com.nf.mvc.util;

import com.nf.mvc.Handler;
import com.nf.mvc.ViewResult;
import com.nf.mvc.exception.exceptions.UnableToExecuteException;

import java.lang.reflect.Method;

public class HandlerInvokeUtils {
    private HandlerInvokeUtils() {
    }

    public static ViewResult invoke(Handler handler, Object... args) {
        Object instance = ReflectionUtils.newInstance(handler.getClz());
        Method method = handler.getMethod();
        Object obj = null;
        try {
            if (handler.getParameters().length == 0) {
                obj = method.invoke(instance);
            } else {
                obj = method.invoke(instance, args);
            }
        } catch (Exception e) {
            throw new UnableToExecuteException("无法调用 "+ method +" 方法 "+e.getMessage(),e);
        }
        return ResultViewResult.getViewResult(obj);
    }

}
