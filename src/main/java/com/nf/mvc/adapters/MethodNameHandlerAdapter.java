package com.nf.mvc.adapters;

import com.nf.mvc.HandlerAdapter;
import com.nf.mvc.HandlerInfo;
import com.nf.mvc.ViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 此HandlerAdapter就是看handler内部有没有一个叫process名字的方法
 * 有就支持，没有就不管
 */
public class MethodNameHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        if(handler instanceof HandlerInfo){

            HandlerInfo handlerInfo = (HandlerInfo)handler;
            Class<?> handlerClass = handlerInfo.getClz();
            try {
                Method method = handlerClass.getDeclaredMethod("process");
                return  method !=null;
            } catch (NoSuchMethodException e) {
               return false;
            }
        }
        return false;
    }

    @Override
    public ViewResult handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        HandlerInfo handlerInfo = (HandlerInfo)handler;
        Class<?> handlerClass = handlerInfo.getClz();

        Object instance = handlerClass.newInstance();
        Method method = handlerClass.getDeclaredMethod("process");
       return (ViewResult) method.invoke(instance);

    }
}
