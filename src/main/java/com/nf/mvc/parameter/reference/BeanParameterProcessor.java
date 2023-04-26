package com.nf.mvc.parameter.reference;

import com.nf.mvc.Handler;
import com.nf.mvc.ParameterProcessor;
import com.nf.mvc.support.Order;
import com.nf.mvc.util.ReflectionUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.rmi.RemoteException;

@Order(4)
public class BeanParameterProcessor implements ParameterProcessor {

    protected Parameter parameter;

    protected Class<?> parameterType;

    @Override
    public boolean supports(Parameter parameter) {
        this.parameter = parameter;
        this.parameterType = parameter.getType();

        return !this.parameterType.isArray() && !this.parameterType.isEnum()
                && !this.parameterType.isPrimitive() && !this.isPackage();
    }

    private boolean isPackage() {
        try {
            return ((Class) this.parameterType.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Object processor(Handler handler, HttpServletRequest req) throws IOException {
        Class<?> paramType = this.parameterType;
        Object instance = ReflectionUtils.newInstance(paramType);
        try {
            BeanUtils.populate(instance, req.getParameterMap());
        } catch (Exception e) {
            throw new RemoteException("无法填充数据",e);
        }
        return instance;
    }
}
