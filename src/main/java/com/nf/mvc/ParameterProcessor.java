package com.nf.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Parameter;

public interface ParameterProcessor {
    boolean supports(MethodParameter methodParameter);

    Object processor(MethodParameter methodParameter, HttpServletRequest req) throws IOException, ServletException;
}
