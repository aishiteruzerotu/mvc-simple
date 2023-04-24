package com.nf.mvc.util.parameter;

import com.nf.mvc.util.AbstractParameterProcessor;

import java.util.Arrays;

public class IntegerParameterProcessor extends AbstractParameterProcessor<Integer> {
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


    protected Object getConvert(Object obj) {
        if(this.parameterType.equals(this.getArrayType())){
            return (Integer) Integer.valueOf(obj.toString());
        }
        return Integer.parseInt(obj.toString());
    }

    protected Object getConverts(String[] strings) {
        int[] ints = Arrays.stream(strings).mapToInt(Integer::parseInt).toArray();
        if (this.parameterType.equals(this.getArrayType())){
            return Arrays.stream(ints).boxed().toArray(Integer[]::new);
        }
        return ints;
    }
}
