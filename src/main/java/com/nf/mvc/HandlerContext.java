package com.nf.mvc;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HandlerContext {

    private static ThreadLocal<HandlerContext> local = new ThreadLocal<>();
    private HttpServletRequest request;
    private HttpServletResponse response;

    public static HandlerContext getContext(){
        //等于null表示当前线程没有一个本地的HandlerContext对象

        if (local.get() == null) {
            HandlerContext context = new HandlerContext();
            local.set(context);
        }
        return local.get();

    }

    HandlerContext setRequest(HttpServletRequest request) {
        this.request = request;
        return  this;
    }

    HandlerContext setResponse(HttpServletResponse response) {
        this.response = response;
        return  this;
    }

    public HttpServletRequest getRequest(){
        return request;
    }

    public HttpServletResponse getResponse(){
        return response;
    }

    public HttpSession getSession(){
        return request.getSession();
    }

    public ServletContext getApplication(){
        return request.getServletContext();
    }

    public void clear(){
        local.remove();
    }
}
