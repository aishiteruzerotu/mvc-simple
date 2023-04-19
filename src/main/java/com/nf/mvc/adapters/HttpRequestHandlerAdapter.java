package com.nf.mvc.adapters;

import com.nf.mvc.*;
import com.nf.mvc.util.HandlerInvokeUtils;
import com.nf.mvc.view.DefaultView;
import com.nf.mvc.view.VoidView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpRequestHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Handler handler) {
        return HttpRequestHandler.class.isAssignableFrom(handler.getClz());
    }

    @Override
    public ViewResult handle(HttpServletRequest req,HttpServletResponse resp,Handler handler) throws Exception {
        return new DefaultView( HandlerInvokeUtils.invoke(handler,req,resp));
    }
}
