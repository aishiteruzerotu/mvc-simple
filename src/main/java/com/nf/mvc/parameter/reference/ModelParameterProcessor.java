package com.nf.mvc.parameter.reference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nf.mvc.MethodParameter;
import com.nf.mvc.ParameterProcessor;
import com.nf.mvc.annotation.RequestModel;
import com.nf.mvc.support.Order;
import com.nf.mvc.util.ObjectMapperUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(5)
public class ModelParameterProcessor implements ParameterProcessor {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return methodParameter.getParameter().isAnnotationPresent(RequestModel.class);
    }

    @Override
    public Object processor(MethodParameter methodParameter, HttpServletRequest req) throws IOException {
        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
        //TODL:List集合反序列化问题
        Class<?> paramType = methodParameter.getParamType();
        return objectMapper.readValue(req.getInputStream(), paramType);
    }
}
