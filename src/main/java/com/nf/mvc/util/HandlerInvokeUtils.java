package com.nf.mvc.util;

import com.nf.mvc.Handler;
import com.nf.mvc.ViewResult;
import com.nf.mvc.view.ToStringViewResult;
import com.nf.mvc.view.VoidViewResult;

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
            System.out.println("无法运行");
        }
        return getViewResult(obj);
    }

    private static ViewResult getViewResult(Object obj) {
        ViewResult view = new VoidViewResult();
        if (obj != null) {
            if (obj instanceof ViewResult) {
                view = (ViewResult) obj;
            } else {
                view = new ToStringViewResult(obj);
            }
        }
        return view;
    }
}
