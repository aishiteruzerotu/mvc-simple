package com.nf.mvc.parameter.reference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nf.mvc.Handler;
import com.nf.mvc.ParameterProcessor;
import com.nf.mvc.annotation.RequestModel;
import com.nf.mvc.support.Order;
import com.nf.mvc.util.ObjectMapperUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Parameter;

@Order(4)
public class ModelParameterProcessor implements ParameterProcessor {

    protected Parameter parameter;

    protected Class<?> parameterType;

    @Override
    public boolean supports(Parameter parameter) {
        this.parameter = parameter;
        this.parameterType = parameter.getType();
        return parameter.isAnnotationPresent(RequestModel.class);
    }

    @Override
    public Object processor(Handler handler, HttpServletRequest req) throws IOException {
        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
        //TODL:List集合反序列化问题
        Class<?> paramType = this.parameterType;
        return objectMapper.readValue(req.getInputStream(), paramType);
    }
}
