package com.nf.mvc;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Parameter;

public interface ParameterProcessor {
    boolean supports(Parameter parameter);

    Object processor(Handler handler, HttpServletRequest req) throws IOException;
}
