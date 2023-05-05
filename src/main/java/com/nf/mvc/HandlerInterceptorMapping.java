package com.nf.mvc;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nf.mvc.annotation.Interceptor;
import com.nf.mvc.annotation.ValueConstants;

import java.util.*;

public class HandlerInterceptorMapping {
    private Cache<String, List<HandlerInterceptor>> handlerInterceptors = Caffeine.newBuilder()
            // 初始数量 20
            .initialCapacity(20)
            // 缓存最大条目数 50
            .maximumSize(50)
            .build();

    private final List<HandlerInterceptor> HANDLER_INTERCEPTORS = MvcContext.getMvcContext().getCustomHandlerInterceptors();

    private final String  DEFAULT_MAPPING_URL = "/*";

    private HandlerInterceptorMapping(){}

    public static HandlerInterceptorMapping getHandlerInterceptorMapping(){
        return new HandlerInterceptorMapping();
    }

    private List<HandlerInterceptor> getHandlerInterceptorList(String url) {
        List<HandlerInterceptor> list = new ArrayList<>();
        for (HandlerInterceptor handlerInterceptor : HANDLER_INTERCEPTORS) {
            if (isMapping(handlerInterceptor,url)){
                list.add(handlerInterceptor);
            }
        }
        return list;
    }

    private boolean isMapping(HandlerInterceptor handlerInterceptor,String url){
        Class<? extends HandlerInterceptor> clz = handlerInterceptor.getClass();
        if (clz.isAnnotationPresent(Interceptor.class)) {
            Interceptor annotation = clz.getAnnotation(Interceptor.class);
            String value = annotation.value();
            if (DEFAULT_MAPPING_URL.equals(value)) {
                return true;
            }

            if (ValueConstants.DEFAULT_NONE.equals(value)){
                String[] values = annotation.values();
                for (String s : values) {
                    if (s.equals(url)){
                        return true;
                    }
                }
                return false;
            }

            return value.equals(url);
        }else {
            return true;
        }
    }

    public List<HandlerInterceptor> getHandlerInterceptors(String url) {
        return handlerInterceptors.get(url, k -> getHandlerInterceptorList(url));
    }
}
