package com.nf.mvc.adapters;

import com.nf.mvc.Handler;

/**
 * 此HandlerAdapter就是看handler内部有没有一个叫process名字的方法
 * 有就支持，没有就不管
 */
public class MethodNameHandlerAdapter extends AbstractHandlerAdapter {
    public MethodNameHandlerAdapter() {
    }

    @Override
    protected boolean isAssignableFrom(Handler handler) {
        return handler.getMethod().getName().equals("process");
    }
}
