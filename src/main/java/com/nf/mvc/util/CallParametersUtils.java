package com.nf.mvc.util;

import com.nf.mvc.*;
import com.nf.mvc.exception.exceptions.UnableToProcessTypeException;
import com.nf.mvc.parameter.HandlerParameterProcessorComposite;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Parameter;

public class CallParametersUtils {
    private CallParametersUtils() {
    }

    private static HandlerParameterProcessorComposite composite = new HandlerParameterProcessorComposite().addResolvers(MvcContext.getMvcContext().getParameterProcessors());

    public static Object[] getObjects(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws ServletException, IOException {
        Parameter[] parameters = handler.getParameters();
        int parameterCount = parameters.length;

        if (parameterCount == 0) {
            return new Object[0];
        }

        Object[] objects = new Object[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            MethodParameter methodParameter = new MethodParameter(handler.getMethod(), i, handler.getParamName(parameters[i]),handler.getClz());
            objects[i] = getObject(methodParameter, req);
        }

        return objects;
    }

    public static Object getObject(MethodParameter methodParameter, HttpServletRequest req) throws IOException, ServletException {
        if (composite.supports(methodParameter)) {
            return composite.processor(methodParameter,req);
        }
        return new UnableToProcessTypeException("无法处理 " + methodParameter.getParamType() + " 类型的参数。可以添加 " +
                ParameterProcessor.class + " 的实现类已支持 " + methodParameter.getParamType() + "参数的处理");
    }

}
