package com.nf.mvc.adapters;

import com.nf.mvc.Handler;
import com.nf.mvc.annotation.RequestMapping;

public class MethodRequestMappingHandlerAdapter extends AbstractHandlerAdapter {
    @Override
    protected boolean isAssignableFrom(Handler handler) {
        return handler.getMethod().isAnnotationPresent(RequestMapping.class);
    }
}
