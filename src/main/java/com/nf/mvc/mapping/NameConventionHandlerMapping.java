package com.nf.mvc.mapping;

import com.nf.mvc.*;
import com.nf.mvc.handler.HandlerDefault;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 依据类的名字来处理映射，
 * 比如你的类是以Controller结尾的，名字是FirstController
 * 那么就表明你能处理的请求地址是:/first
 */
public class NameConventionHandlerMapping implements HandlerMapping {

    /** 类名的后缀 */
    private static final String SUFFIX = "Controller";
    /** 此map中放置的是当前HandlerMapping所能处理的所有请求 */
    private Map<String, Handler> handlers = new HashMap<>();


    public NameConventionHandlerMapping() {
        List<Class<?>> allScanedClasses = MvcContext.getMvcContext().getAllScanedClasses();
        for (Class<?> clz : allScanedClasses) {
            //com.FirstController(类的全程）--->FirstController（简单名）
            String simpleName= clz.getSimpleName();
            if(simpleName.endsWith(SUFFIX)){
                Method[] methods = clz.getDeclaredMethods();
                Method method = methods[0];
                if (method==null){
                    continue;
                }
                String uri = this.generateHandleUrl( simpleName);
                HandlerDefault handlerDefault = new HandlerDefault();
                handlerDefault.setClz(clz);
                handlerDefault.setMethod(method);
                handlerDefault.setParameters(method.getParameters());
                handlers.put(uri,handlerDefault);
            }
        }
    }

    private String generateHandleUrl(String simpleName) {
        return ("/" + simpleName.substring(0, simpleName.length() - SUFFIX.length())).toLowerCase(Locale.ROOT);
    }

    @Override
    public Handler getHandler(String uri) throws Exception{
        return handlers.get(uri);
    }
}
