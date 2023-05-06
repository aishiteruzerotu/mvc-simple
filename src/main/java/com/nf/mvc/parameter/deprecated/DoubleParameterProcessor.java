package com.nf.mvc.parameter.deprecated;

import com.nf.mvc.parameter.AbstractDeprecatedParameterProcessor;

import java.util.Arrays;

public class DoubleParameterProcessor extends AbstractDeprecatedParameterProcessor<Double> {
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
    protected Object getConvert(String str) {
        if(this.parameterType.equals(this.getType())){
            return (Double) Double.valueOf(str);
        }
        return Double.parseDouble(str);
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
