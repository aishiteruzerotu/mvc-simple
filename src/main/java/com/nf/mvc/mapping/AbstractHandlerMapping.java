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
        return handlers.get(uri);
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
        handlers.put(uri,handler);
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
