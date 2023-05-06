package com.nf.mvc.parameter.deprecated;

import com.nf.mvc.parameter.AbstractDeprecatedParameterProcessor;

import java.lang.reflect.Parameter;

public class StringEnumParameterProcessor extends AbstractDeprecatedParameterProcessor<Enum> {
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
