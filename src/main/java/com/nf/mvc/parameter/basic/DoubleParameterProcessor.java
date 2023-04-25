package com.nf.mvc.parameter.basic;

import com.nf.mvc.AbstractParameterProcessor;

import java.util.Arrays;

public class DoubleParameterProcessor extends AbstractParameterProcessor<Double> {
    @Override
    protected Class<? extends Double> getType() {
        return Double.class;
    }

    @Override
    protected Class<? extends Double[]> getArrayType() {
        return Double[].class;
    }

    @Override
    protected boolean basicType() {
        return this.parameterType.equals(double.class) || this.parameterType.equals(double[].class);
    }

    @Override
    protected Object getConvert(Object obj) {
        if(this.parameterType.equals(this.getType())){
            return (Double) Double.valueOf(obj.toString());
        }
        return Double.parseDouble(obj.toString());
    }

    @Override
    protected Object getConverts(String[] strings) {
        double[] doubles = Arrays.stream(strings).mapToDouble(Double::parseDouble).toArray();
        if (this.parameterType.equals(this.getArrayType())){
            return Arrays.stream(doubles).boxed().toArray(Double[]::new);
        }
        return doubles;
    }
}
