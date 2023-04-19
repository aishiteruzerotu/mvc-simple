package com.nf.mvc;

public interface HandlerMapping {
    Handler getHandler(String uri) throws Exception;
}
