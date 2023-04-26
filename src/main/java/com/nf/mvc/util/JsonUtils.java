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

    public static  <T> T read(InputStream stream, Class<T> vo ) throws IOException {
        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
        return objectMapper.readValue(stream, vo);
    }
}
