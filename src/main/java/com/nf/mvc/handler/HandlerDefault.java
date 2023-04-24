package com.nf.mvc.handler;

import com.nf.mvc.Handler;
import com.nf.mvc.util.CallParametersUtils;

import java.util.List;

public class HandlerDefault extends Handler {
    public HandlerDefault() {
    }

    @Override
    protected void init() {
        if (this.getClz()==null||this.getParameters()==null||this.getMethod()==null){
            return;
        }

        List<String> paramNames = CallParametersUtils.getParamNames(this.getClz(),
                this.getMethod().getName(), this.getParamType());
        for (int i = 0; i < this.parameters.length; i++) {
            if (paramNames!=null)
                this.parameterNameMapping.put(this.parameters[i], paramNames.get(i));
        }
    }

    private Class<?>[] getParamType() {
        Class<?>[] classes = new Class[parameters.length];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = parameters[i].getType();
        }
        return classes;
    }


}
