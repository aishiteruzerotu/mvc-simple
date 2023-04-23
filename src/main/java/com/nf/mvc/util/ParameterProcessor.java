package com.nf.mvc.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Parameter;

public interface ParameterProcessor {
    boolean supports(Parameter parameter);

    Object getObject(Object obj) ;
}
