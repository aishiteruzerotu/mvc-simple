package com.nf.mvc.mapping;

import com.nf.mvc.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Locale;

public class MethodRequestMappingHandlerMapping extends AbstractHandlerMapping {
    public MethodRequestMappingHandlerMapping() {
    }

    @Override
    protected String getUri(Class<?> clz, Method method) {
        return method.getAnnotation(RequestMapping.class).value().toLowerCase(Locale.ROOT);
    }
}
