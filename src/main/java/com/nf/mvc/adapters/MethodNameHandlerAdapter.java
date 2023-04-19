package com.nf.mvc.adapters;

import com.nf.mvc.Handler;
import com.nf.mvc.HandlerAdapter;
import com.nf.mvc.HandlerInfo;
import com.nf.mvc.ViewResult;
import com.nf.mvc.annotation.RequestParam;
import com.nf.mvc.util.CallParametersUtils;
import com.nf.mvc.util.HandlerInvokeUtils;
import com.nf.mvc.view.DefaultView;
import com.nf.mvc.view.VoidView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * 此HandlerAdapter就是看handler内部有没有一个叫process名字的方法
 * 有就支持，没有就不管
 */
public class MethodNameHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Handler handler) {
        return handler.getMethod().getName().equals("process");
    }

    @Override
    public ViewResult handle(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws Exception {
        Object[] objects = CallParametersUtils.getObjects(req ,resp, handler);
        return new DefaultView(HandlerInvokeUtils.invoke(handler, objects));
    }


}
