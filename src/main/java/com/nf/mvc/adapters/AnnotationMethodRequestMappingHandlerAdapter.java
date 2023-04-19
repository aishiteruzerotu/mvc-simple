package com.nf.mvc.adapters;

import com.nf.mvc.Handler;
import com.nf.mvc.HandlerAdapter;
import com.nf.mvc.ViewResult;
import com.nf.mvc.annotation.RequestMapping;
import com.nf.mvc.util.CallParametersUtils;
import com.nf.mvc.util.HandlerInvokeUtils;
import com.nf.mvc.view.DefaultView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationMethodRequestMappingHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Handler handler) {
        return handler.getMethod().isAnnotationPresent(RequestMapping.class);
    }

    @Override
    public ViewResult handle(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws Exception {
        Object[] objects = CallParametersUtils.getObjects(req ,resp, handler);
        return new DefaultView(HandlerInvokeUtils.invoke(handler, objects));
    }
}
