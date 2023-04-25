package com.nf.mvc.parameter.basic;

import com.nf.mvc.AbstractParameterProcessor;

public class CharParameterProcessor extends AbstractParameterProcessor<Character> {
    @Override
    protected Class<? extends Character> getType() {
        return Character.class;
    }

    @Override
    protected Class<? extends Character[]> getArrayType() {
        return Character[].class;
    }

    @Override
    protected boolean basicType() {
        return this.parameterType.equals(char.class) || this.parameterType.equals(char[].class);
    }

    @Override
    protected Object getConvert(String str) {
        char c = str.charAt(str.length());
        if(this.parameterType.equals(this.getArrayType())){
            return (Character) c;
        }
        return c;
    }

    @Override
    protected Object getConverts(String[] strings) {
        Character[] bytes = this.toArr(new Character[strings.length], strings);
        if (this.parameterType.equals(this.getArrayType())){
            return bytes;
        }
        return this.transform(bytes);
    }

    @Override
    protected Character getT(String str) {
        return str.charAt(str.length());
    }

    protected char[] transform(Character[] characters){
        char[] result = new char[characters.length];
        for (int i = 0; i < characters.length; i++) {
            result[i] = characters[i];
        }
        return result;
    }
}
