package com.nf.mvc.util.parameter.basic;

import com.nf.mvc.util.AbstractParameterProcessor;

public class ShortParameterProcessor extends AbstractParameterProcessor<Short> {
    @Override
    protected Class<? extends Short> getType() {
        return Short.class;
    }

    @Override
    protected Class<? extends Short[]> getArrayType() {
        return Short[].class;
    }

    @Override
    protected boolean basicType() {
        return this.parameterType.equals(short.class) || this.parameterType.equals(short[].class);
    }

    @Override
    protected Object getConvert(Object obj) {
        if(this.parameterType.equals(this.getArrayType())){
            return (Short) Short.valueOf(obj.toString());
        }
        return Short.parseShort(obj.toString());
    }

    @Override
    protected Object getConverts(String[] strings) {
        Short[] bytes = this.toArr(new Short[strings.length], strings);
        if (this.parameterType.equals(this.getArrayType())){
            return bytes;
        }
        return this.transform(bytes);
    }

    @Override
    protected Short getT(String str) {
        return Short.valueOf(str);
    }

    protected short[] transform(Short[] shorts){
        short[] result = new short[shorts.length];
        for (int i = 0; i < shorts.length; i++) {
            result[i] = shorts[i];
        }
        return result;
    }
}
