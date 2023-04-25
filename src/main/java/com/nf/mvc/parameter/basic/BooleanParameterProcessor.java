package com.nf.mvc.parameter.basic;

import com.nf.mvc.AbstractParameterProcessor;

public class BooleanParameterProcessor extends AbstractParameterProcessor<Boolean> {
    @Override
    protected Class<? extends Boolean> getType() {
        return Boolean.class;
    }

    @Override
    protected Class<? extends Boolean[]> getArrayType() {
        return Boolean[].class;
    }

    @Override
    protected boolean basicType() {
        return this.parameterType.equals(boolean.class) || this.parameterType.equals(boolean[].class);
    }

    @Override
    protected Object getConvert(String str) {
        if(this.parameterType.equals(this.getType())){
            return (Boolean) Boolean.valueOf(str);
        }
        return Boolean.parseBoolean(str);
    }

    @Override
    protected Object getConverts(String[] strings) {
        Boolean[] booleans = this.toArr(new Boolean[strings.length], strings);
        if (this.parameterType.equals(this.getArrayType())){
            return booleans;
        }
        return this.transform(booleans);
    }

    @Override
    protected Boolean getT(String str) {
        return Boolean.valueOf(str);
    }

    protected boolean[] transform(Boolean[] booleans){
        boolean[] result = new boolean[booleans.length];
        for (int i = 0; i < booleans.length; i++) {
            result[i] = booleans[i];
        }
        return result;
    }
}
