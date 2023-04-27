package com.nf.mvc.exception;

import com.nf.mvc.Handler;
import com.nf.mvc.HandlerExceptionResolver;
import com.nf.mvc.ViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 打印异常信息
 * 不推荐使用，因为该类只打印了异常
 */
@Deprecated
public class PrintStackTraceHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ViewResult resolveException(HttpServletRequest request, HttpServletResponse response, Handler handler, Exception ex) {
        ex.printStackTrace();
        return null;
    }
}
