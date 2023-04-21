package com.nf.mvc.adapters;

import com.nf.mvc.Handler;
import com.nf.mvc.HandlerAdapter;
import com.nf.mvc.ViewResult;
import com.nf.mvc.util.CallParametersUtils;
import com.nf.mvc.util.HandlerInvokeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractHandlerAdapter implements HandlerAdapter {
    public AbstractHandlerAdapter() {
    }

    @Override
    public boolean supports(Handler handler) {
        return  handler!=null && handler instanceof Handler && this.isAssignableFrom(handler);
    }

    protected abstract boolean isAssignableFrom(Handler handler);

    @Override
    public ViewResult handle(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws Exception {
        return this.getViewResult(req, resp, handler);
    }

    protected ViewResult getViewResult(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws ServletException, IOException {
        Object[] objects = CallParametersUtils.getObjects(req, resp, handler);
        return HandlerInvokeUtils.invoke(handler, objects);
    }
}
