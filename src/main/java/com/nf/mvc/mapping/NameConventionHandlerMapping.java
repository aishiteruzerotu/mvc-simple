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
public class NameConventionHandlerMapping extends AbstractHandlerMapping {

    /** 类名的后缀 */
    private static final String SUFFIX = "Controller";

    public NameConventionHandlerMapping() {
    }

    @Override
    protected void setHandlers(Class<?> clz) {
        //com.FirstController(类的全程）--->FirstController（简单名）
        String simpleName= clz.getSimpleName();
        if(simpleName.endsWith(SUFFIX)){
            Method[] methods = clz.getDeclaredMethods();
            Method method = methods[0];
            if (method==null){
                return;
            }
            this.setHandlers(clz,method);
        }
    }
    @Override
    protected String getUri(Class<?> clz, Method method) {
        String simpleName = clz.getSimpleName();
        return this.generateHandleUrl( simpleName);
    }

    private String generateHandleUrl(String simpleName) {
        return ("/" + simpleName.substring(0, simpleName.length() - SUFFIX.length())).toLowerCase(Locale.ROOT);
    }
}
