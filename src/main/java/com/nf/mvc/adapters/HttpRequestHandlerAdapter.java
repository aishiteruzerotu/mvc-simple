package com.nf.mvc.adapters;

import com.nf.mvc.*;
import com.nf.mvc.util.HandlerInvokeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpRequestHandlerAdapter extends AbstractHandlerAdapter {
    public HttpRequestHandlerAdapter() {
    }

    @Override
    protected boolean isAssignableFrom(Handler handler) {
        return HttpRequestHandler.class.isAssignableFrom(handler.getClz());
    }

    @Override
    protected ViewResult getViewResult(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws ServletException, IOException {
        return HandlerInvokeUtils.invoke(handler,req,resp);
    }
}
