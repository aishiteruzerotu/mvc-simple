package com.nf.mvc.parameter.reference;

import com.nf.mvc.AbstractParameterProcessor;
import com.nf.mvc.support.Order;

import java.lang.reflect.Parameter;

@Order(2)
public class StringEnumParameterProcessor extends AbstractParameterProcessor<Enum> {
    @Override
    protected boolean isType(Parameter parameter) {
        return parameter.getType().isEnum();
    }

    @Override
    protected Object getConvert(String str) {
        return Enum.valueOf(this.parameterType.asSubclass(Enum.class), str);
    }

    @Override
    protected Object getConverts(String[] strings) {
        return null;
    }
}
