package com.nf.mvc.parameter.basic;

import com.nf.mvc.AbstractParameterProcessor;

public class ByteParameterProcessor extends AbstractParameterProcessor<Byte> {
    @Override
    protected Class<? extends Byte> getType() {
        return Byte.class;
    }

    @Override
    protected Class<? extends Byte[]> getArrayType() {
        return Byte[].class;
    }

    @Override
    protected boolean basicType() {
        return this.parameterType.equals(byte.class) || this.parameterType.equals(byte[].class);
    }

    @Override
    protected Object getConvert(String str) {
        if(this.parameterType.equals(this.getType())){
            return (Byte) Byte.valueOf(str);
        }
        return Byte.parseByte(str);
    }

    @Override
    protected Object getConverts(String[] strings) {
        Byte[] bytes = this.toArr(new Byte[strings.length], strings);
        if (this.parameterType.equals(this.getArrayType())){
            return bytes;
        }
        return this.transform(bytes);
    }

    @Override
    protected Byte getT(String str) {
        return Byte.valueOf(str);
    }

    protected byte[] transform(Byte[] bytes){
        byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = bytes[i];
        }
        return result;
    }
}
