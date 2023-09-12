package com.nf.mvc.mapping;

import com.nf.mvc.Handler;
import com.nf.mvc.HandlerMapping;
import com.nf.mvc.MvcContext;
import com.nf.mvc.annotation.RequestMapping;
import com.nf.mvc.exception.exceptions.RepeatException;
import com.nf.mvc.handler.HandlerDefault;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractHandlerMapping implements HandlerMapping {
    protected Map<String, Handler> handlers = new HashMap<>();

    public AbstractHandlerMapping() {
        init();
    }

    public Handler getHandler(HttpServletRequest req) {
        String uri = this.getUri(req).toLowerCase(Locale.ROOT);
        return this.handlers.get(uri);
    }

    protected String getUri(HttpServletRequest req) {
        String contextPath = req.getContextPath();
        return req.getRequestURI().substring(contextPath.length());
    }

    protected void init() {
        List<Class<?>> classList = MvcContext.getMvcContext().getAllScanedClasses();
        for (Class<?> clz : classList) {
            this.setHandlers(clz);
        }
    }

    protected void setHandlers(Class<?> clz) {
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                this.setHandlers(clz, method);
            }
        }
    }

    protected void setHandlers(Class<?> clz, Method method) {
        String uri = getUri(clz, method);
        Handler handler = getHandler(clz, method);
        this.checkNullURIMapping(clz, method, uri);
        this.handlers.put(uri, handler);
    }

    /**
     * 检测是否存在相同的 URI
     * @param clz 类对象
     * @param method 方法对象
     * @param uri 请求映射
     */
    protected void checkNullURIMapping(Class<?> clz, Method method, String uri) {
        if (this.handlers.get(uri)!=null) {
            throw new RepeatException("不能出现重复的映射， " + clz + " 的 " + method.getName() + " 方法的 " + uri + " 映射重复");
        }
    }

    /**
     * 配置 Handler
     * @param clz 对应类
     * @param method 对应方法
     * @return Handler 执行器
     */
    protected Handler getHandler(Class<?> clz, Method method) {
        Handler handlerDefault = new HandlerDefault();
        handlerDefault.setClz(clz);
        handlerDefault.setMethod(method);
        handlerDefault.setParameters(method.getParameters());
        return handlerDefault;
    }

    protected abstract String getUri(Class<?> clz, Method method);
}
