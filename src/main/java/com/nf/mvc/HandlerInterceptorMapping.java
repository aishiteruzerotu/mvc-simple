package com.nf.mvc;

import com.nf.mvc.annotation.Interceptor;
import com.nf.mvc.annotation.ValueConstants;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class HandlerInterceptorMapping {
    private static Map<String, List<HandlerInterceptor>> handlerInterceptorMaps = new HashMap<>();

    private static final List<HandlerInterceptor> HANDLER_INTERCEPTORS = MvcContext.getMvcContext().getCustomHandlerInterceptors();

    public static final String  DEFAULT_MAPPING_URL = "/*";

    static {
        List<HandlerInterceptor> list = getHandlerInterceptorList(DEFAULT_MAPPING_URL);
        handlerInterceptorMaps.put(DEFAULT_MAPPING_URL, list);
    }

    private static List<HandlerInterceptor> getHandlerInterceptorList(String url) {
        List<HandlerInterceptor> list = new ArrayList<>();
        for (HandlerInterceptor handlerInterceptor : HANDLER_INTERCEPTORS) {
            if (isMapping(handlerInterceptor,url)){
                list.add(handlerInterceptor);
            }
        }
        return list;
    }

    private static boolean isMapping(HandlerInterceptor handlerInterceptor,String url){
        if (DEFAULT_MAPPING_URL.equals(url)){
            return true;
        }
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

    public static List<HandlerInterceptor> getHandlerInterceptors(String url) {
        List<HandlerInterceptor> handlerInterceptors = handlerInterceptorMaps.get(url);

        if (handlerInterceptors == null) {
            handlerInterceptorMaps.put(url, getHandlerInterceptorList(url));
            Set<HandlerInterceptor> set = new HashSet<>();
            set.addAll(handlerInterceptorMaps.get(DEFAULT_MAPPING_URL));
            set.addAll(handlerInterceptorMaps.get(url));
            //TODO:待解决重复映射问题
            handlerInterceptors = new ArrayList<>(set);
        }

        return handlerInterceptors;
    }
}
