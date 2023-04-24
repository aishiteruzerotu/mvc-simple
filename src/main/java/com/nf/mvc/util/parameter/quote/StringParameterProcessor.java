package com.nf.mvc.util.parameter.quote;

import com.nf.mvc.support.Order;
import com.nf.mvc.util.AbstractParameterProcessor;

/**
 * Parameter 的类型是 String 类型的则不需要任何处理
 */
@Order(1)
public class StringParameterProcessor extends AbstractParameterProcessor<String> {
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
    protected Object getConvert(Object obj) {
        return obj;
    }

    @Override
    protected Object getConverts(String[] strings) {
        return strings;
    }
}
