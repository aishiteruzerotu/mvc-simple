package com.nf.mvc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtils {

    public static String write(Object data){
        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
        String valueAsString = null;
        try {
            valueAsString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json序列化出错",e);
        }
        return valueAsString;
    }

    public static  <T> T read(InputStream stream, Class<T> vo ){
        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
        T result = null;
        try {
            result = objectMapper.readValue(stream, vo);
        } catch (IOException e) {
            throw new RuntimeException("json反序列化出错",e);
        }
        return result;
    }
}
