package com.nf.mvc.util;

import com.nf.mvc.*;
import com.nf.mvc.exception.exceptions.UnableToProcessTypeException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.List;

public class CallParametersUtils {
    private CallParametersUtils() {
    }

    private static List<ParameterProcessor> parameterProcessorList = MvcContext.getMvcContext().getParameterProcessors();

    public static Object[] getObjects(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws ServletException, IOException {
        Parameter[] parameters = handler.getParameters();
        int parameterCount = parameters.length;

        if (parameterCount == 0) {
            return new Object[0];
        }

        Object[] objects = new Object[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            objects[i] = getObject(handler, parameters[i], req);
        }

        return objects;
    }

    public static Object getObject(Handler handler, Parameter parameter, HttpServletRequest req) throws IOException, ServletException {
        for (ParameterProcessor parameterProcessor : parameterProcessorList) {
            if (parameterProcessor.supports(parameter)) {
                return parameterProcessor.processor(handler, req);
            }
        }
        return new UnableToProcessTypeException("无法处理 " + parameter.getType() + " 类型的参数。可以添加 " +
                ParameterProcessor.class + " 的实现类已支持 " + parameter.getType() + "参数的处理");
    }

}
