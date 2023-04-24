package com.nf.mvc.mapping;

import com.nf.mvc.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * 不推荐使用该类，应该使用 com.nf.mvc.mapping.RequestControllerHandlerMapping 类
 * 该类只针对 方法
 * 而 RequestControllerHandlerMapping 类可以同时应用 类和方法
 */
@Deprecated
public class MethodRequestMappingHandlerMapping extends AbstractHandlerMapping {
    public MethodRequestMappingHandlerMapping() {
    }

    @Override
    protected String getUri(Class<?> clz, Method method) {
        return method.getAnnotation(RequestMapping.class).value().toLowerCase(Locale.ROOT);
    }

    /**
     * 该类是以方法为准的，也就是以方法的 RequestMapping 的映射为准，所以必然会出现相同，所以返回假
     * 当前类必然会被覆盖，所以没关系
     * @param clz 类对象
     * @param method 方法对象
     * @param uri 请求映射
     * @return 假
     */
    @Override
    protected boolean isNullURIMapping(Class<?> clz, Method method, String uri) {
        return false;
    }
}
