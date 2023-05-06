package com.nf.mvc.parameter.deprecated;

import com.nf.mvc.parameter.AbstractDeprecatedParameterProcessor;

import java.util.Arrays;

public class IntegerParameterProcessor extends AbstractDeprecatedParameterProcessor<Integer> {
    @Override
    protected Class<? extends Integer> getType() {
        return Integer.class;
    }

    @Override
    protected Class<? extends Integer[]> getArrayType() {
        return Integer[].class;
    }

    @Override
    protected boolean basicType() {
        return this.parameterType.equals(int.class) || this.parameterType.equals(int[].class);
    }

    @Override
    protected Object getConvert(String str) {
        if(this.parameterType.equals(this.getArrayType())){
            return (Integer) Integer.valueOf(str);
        }
        return Integer.parseInt(str);
    }

    @Override
    protected Object getConverts(String[] strings) {
        int[] ints = Arrays.stream(strings).mapToInt(Integer::parseInt).toArray();
        if (this.parameterType.equals(this.getArrayType())){
            return Arrays.stream(ints).boxed().toArray(Integer[]::new);
        }
        return ints;
    }
}
