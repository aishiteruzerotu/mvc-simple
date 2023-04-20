package com.nf.mvc.adapters;

import com.nf.mvc.Handler;
import com.nf.mvc.HandlerAdapter;
import com.nf.mvc.ViewResult;
import com.nf.mvc.util.CallParametersUtils;
import com.nf.mvc.util.HandlerInvokeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 此HandlerAdapter就是看handler内部有没有一个叫process名字的方法
 * 有就支持，没有就不管
 */
public class MethodNameHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Handler handler) {
        return handler!=null && handler instanceof Handler &&  handler.getMethod().getName().equals("process");
    }

    @Override
    public ViewResult handle(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws Exception {
        Object[] objects = CallParametersUtils.getObjects(req ,resp, handler);
        return HandlerInvokeUtils.invoke(handler, objects);
    }


}
