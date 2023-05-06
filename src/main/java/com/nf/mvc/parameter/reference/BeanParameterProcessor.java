package com.nf.mvc.parameter.reference;

import com.nf.mvc.MethodParameter;
import com.nf.mvc.ParameterProcessor;
import com.nf.mvc.support.Order;
import com.nf.mvc.util.ReflectionUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.rmi.RemoteException;

@Order(6)
public class BeanParameterProcessor implements ParameterProcessor {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        Class<?> paramType = methodParameter.getParamType();

        return !paramType.isArray() && !paramType.isEnum()
                && !paramType.isPrimitive() && !this.isPackage(paramType);
    }

    private boolean isPackage(Class<?> paramType) {
        try {
            return ((Class) paramType.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Object processor(MethodParameter methodParameter, HttpServletRequest req) throws IOException {
        Class<?> paramType = methodParameter.getParamType();
        Object instance = ReflectionUtils.newInstance(paramType);
        try {
            BeanUtils.populate(instance, req.getParameterMap());
        } catch (Exception e) {
            throw new RemoteException("无法填充数据", e);
        }
        return instance;
    }
}
