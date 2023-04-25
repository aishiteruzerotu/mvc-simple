package com.nf.mvc.parameter.quote;

import com.nf.mvc.Handler;
import com.nf.mvc.ParameterProcessor;
import com.nf.mvc.annotation.RequestParam;
import com.nf.mvc.support.Order;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Parameter;

@Order(2)
public class StringEnumParameterProcessor implements ParameterProcessor {
    protected Parameter parameter;

    protected Class<?> parameterType;

    @Override
    public boolean supports(Parameter parameter) {
        this.parameter = parameter;
        this.parameterType = parameter.getType();
        return parameter.getType().isEnum();
    }

    @Override
    public Object processor(Handler handler, HttpServletRequest req) throws IOException {
        String key = getKey(handler);
        return getConvert(req.getParameter(key));
    }

    private String getKey(Handler handler) {
        if (parameter.isAnnotationPresent(RequestParam.class)) {
            return parameter.getAnnotation(RequestParam.class).value();
        } else {
            return handler.getParamName(parameter);
        }
    }

    private Object getConvert(String str) {
        return Enum.valueOf(this.parameterType.asSubclass(Enum.class), str);
    }
}
