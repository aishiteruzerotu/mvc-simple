package com.nf.mvc.adapters;

import com.nf.mvc.*;
import com.nf.mvc.util.HandlerInvokeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpRequestHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Handler handler) {
        return  handler!=null && handler instanceof Handler &&  HttpRequestHandler.class.isAssignableFrom(handler.getClz());
    }

    @Override
    public ViewResult handle(HttpServletRequest req,HttpServletResponse resp,Handler handler) throws Exception {
        return HandlerInvokeUtils.invoke(handler,req,resp);
    }
}
