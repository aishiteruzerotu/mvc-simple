package com.nf.mvc.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;
/**
 * 在https://juejin.cn/post/6844904166809157639有对jackson的入门教程
 * https://www.baeldung.com/jackson-serialize-dates(有关于日期的处理教程）
 * https://www.baeldung.com/jackson
 */
public class ObjectMapperUtils {
    public static ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        //需要在pom中添加jackson-datatype-jsr310依赖，以便让其支持jdk8的相关时间API的支持
        //objectMapper.registerModule(new JavaTimeModule());
        //不把日期序列化为时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //设置日期序列化格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //不对null值进行序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //json文本的数据比pojo类多了一些内容，不报错，忽略这些多余的位置数据
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
