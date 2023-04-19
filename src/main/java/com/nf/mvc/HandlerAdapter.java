package com.nf.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    boolean supports(Handler handler);

    ViewResult handle(HttpServletRequest req, HttpServletResponse resp,Handler handler) throws Exception;
}
