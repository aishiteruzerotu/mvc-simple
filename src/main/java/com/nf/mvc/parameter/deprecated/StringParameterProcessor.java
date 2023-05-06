package com.nf.mvc.parameter.deprecated;

import com.nf.mvc.parameter.AbstractDeprecatedParameterProcessor;

/**
 * Parameter 的类型是 String 类型的则不需要任何处理
 */
public class StringParameterProcessor extends AbstractDeprecatedParameterProcessor<String> {
    @Override
    protected Class<? extends String> getType() {
        return String.class;
    }

    @Override
    protected Class<? extends String[]> getArrayType() {
        return String[].class;
    }

    @Override
    protected boolean basicType() {
        return false;
    }

    @Override
    protected Object getConvert(String str) {
        return str;
    }

    @Override
    protected Object getConverts(String[] strings) {
        return strings;
    }
}
