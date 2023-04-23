package com.nf.mvc.util;

import java.lang.reflect.Parameter;

public interface ParameterProcessor {
    boolean supports(Parameter parameter);

    Object convert(Object obj) ;
}
