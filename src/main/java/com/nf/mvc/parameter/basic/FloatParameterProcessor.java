package com.nf.mvc.parameter.basic;

import com.nf.mvc.AbstractParameterProcessor;

public class FloatParameterProcessor extends AbstractParameterProcessor<Float> {
    @Override
    protected Class<? extends Float> getType() {
        return Float.class;
    }

    @Override
    protected Class<? extends Float[]> getArrayType() {
        return Float[].class;
    }

    @Override
    protected boolean basicType() {
        return this.parameterType.equals(float.class) || this.parameterType.equals(float[].class);
    }

    @Override
    protected Object getConvert(String str) {
        if(this.parameterType.equals(this.getType())){
            return (Float) Float.valueOf(str);
        }
        return Float.parseFloat(str);
    }

    @Override
    protected Object getConverts(String[] strings) {
        Float[] bytes = this.toArr(new Float[strings.length], strings);
        if (this.parameterType.equals(this.getArrayType())){
            return bytes;
        }
        return this.transform(bytes);
    }

    @Override
    protected Float getT(String str) {
        return Float.valueOf(str);
    }

    protected float[] transform(Float[] floats){
        float[] result = new float[floats.length];
        for (int i = 0; i < floats.length; i++) {
            result[i] = floats[i];
        }
        return result;
    }
}
