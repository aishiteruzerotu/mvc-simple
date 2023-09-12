package com.nf.mvc;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    Handler getHandler(HttpServletRequest req) throws Exception;
}
