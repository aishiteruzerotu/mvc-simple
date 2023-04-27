package com.nf.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerExceptionResolver {
    ViewResult resolveException(HttpServletRequest req, HttpServletResponse resp, Handler handler, Exception ex);
}
