package com.nf.mvc.mapping;

import com.nf.mvc.Handler;
import com.nf.mvc.HandlerMapping;
import com.nf.mvc.MvcContext;
import com.nf.mvc.annotation.RequestMapping;
import com.nf.mvc.handler.HandlerDefault;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractHandlerMapping implements HandlerMapping {
    protected Map<String, Handler> handlers = new HashMap<>();

    public AbstractHandlerMapping() {
        init();
    }

    public Handler getHandler(String uri) {
        return this.handlers.get(uri);
    }

    protected void init() {
        List<Class<?>> classList = MvcContext.getMvcContext().getAllScanedClasses();
        for (Class<?> clz : classList) {
            this.setHandlers(clz);
        }
    }

    protected void setHandlers(Class<?> clz) {
        Method[] methods = clz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.isAnnotationPresent(RequestMapping.class)) {
                this.setHandlers(clz, method);
            }
        }
    }

    protected void setHandlers(Class<?> clz, Method method) {
        String uri = getUri(clz, method);
        Handler handler = getHandler(clz, method);
        this.isNullURIMapping(clz, method, uri);
        this.handlers.put(uri, handler);
    }

    /**
     * 判断 URI 是否有重复值，没有返回真，有抛出异常
     * @param clz 类对象
     * @param method 方法对象
     * @param uri 请求映射
     * @return 真
     */
    protected boolean isNullURIMapping(Class<?> clz, Method method, String uri) {
        if (this.handlers.get(uri)!=null) {
            throw new RuntimeException("不能出现重复的映射， " + clz + " 的 " + method.getName() + " 方法的 " + uri + " 映射重复");
        }
        return true;
    }

    protected Handler getHandler(Class<?> clz, Method method) {
        Handler handlerDefault = new HandlerDefault();
        handlerDefault.setClz(clz);
        handlerDefault.setMethod(method);
        handlerDefault.setParameters(method.getParameters());
        return handlerDefault;
    }

    protected abstract String getUri(Class<?> clz, Method method);
}
