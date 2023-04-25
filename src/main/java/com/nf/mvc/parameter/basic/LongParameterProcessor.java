package com.nf.mvc.parameter.basic;

import com.nf.mvc.AbstractParameterProcessor;

import java.util.Arrays;

public class LongParameterProcessor extends AbstractParameterProcessor<Long> {
    @Override
    protected Class<? extends Long> getType() {
        return Long.class;
    }

    @Override
    protected Class<? extends Long[]> getArrayType() {
        return Long[].class;
    }

    @Override
    protected boolean basicType() {
        return this.parameterType.equals(long.class) || this.parameterType.equals(long[].class);
    }

    @Override
    protected Object getConvert(Object obj) {
        if(this.parameterType.equals(this.getType())){
            return (Long) Long.valueOf(obj.toString());
        }
        return Long.parseLong(obj.toString());
    }

    @Override
    protected Object getConverts(String[] strings) {
        long[] doubles = Arrays.stream(strings).mapToLong(Long::parseLong).toArray();
        if (this.parameterType.equals(this.getArrayType())){
            return Arrays.stream(doubles).boxed().toArray(Long[]::new);
        }
        return doubles;
    }
}
