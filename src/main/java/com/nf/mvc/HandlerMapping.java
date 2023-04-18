package com.nf.mvc;

public interface HandlerMapping {
    Object getHandler(String uri) throws Exception;
}
