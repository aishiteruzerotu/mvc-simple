package com.nf.mvc.mapping;

import com.nf.mvc.Handler;
import com.nf.mvc.HandlerDefault;
import com.nf.mvc.HandlerMapping;
import com.nf.mvc.MvcContext;
import com.nf.mvc.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationMethodRequestMappingHandlerMapping implements HandlerMapping {
    protected Map<String, HandlerDefault> handlerDefaultMap = new HashMap<>();

    public AnnotationMethodRequestMappingHandlerMapping() {
        init();
    }

    public void init(){
        List<Class<?>> allScanedClasses = MvcContext.getMvcContext().getAllScanedClasses();
        for (Class<?> clz : allScanedClasses) {
            Method[] methods = clz.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    String uri = method.getAnnotation(RequestMapping.class).value().toLowerCase();
                    HandlerDefault handlerDefault = new HandlerDefault();
                    handlerDefault.setClz(clz);
                    handlerDefault.setMethod(method);
                    handlerDefault.setParameters(method.getParameters());
                    handlerDefaultMap.put(uri,handlerDefault);
                }
            }
        }
    }

    public Handler getHandler(String uri) {
        return handlerDefaultMap.get(uri);
    }
}
