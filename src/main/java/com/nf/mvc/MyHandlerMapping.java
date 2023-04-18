package com.nf.mvc;

import com.nf.mvc.annotation.RequestMapping;
import com.nf.mvc.util.ScanUtils;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MyHandlerMapping {
    protected Map<String, HandlerBeanAdapter> handlerAdapterMap = new HashMap<>();

    private static final String SUFFIX = "Controller";
    protected Map<String,Object> handlers = new HashMap<>();
    public MyHandlerMapping(String scanPackage) {
//        this.init(scanPackage);
        ScanResult scanResult = ScanUtils.scan(scanPackage);
        ClassInfoList classInfos = scanResult.getAllAnnotations();

        for (ClassInfo classInfo : classInfos) {
            //FirstController
            String simpleName= classInfo.getSimpleName();
            if(simpleName.endsWith(SUFFIX)){
                String url ="/"+ simpleName.substring(0, simpleName.length() - SUFFIX.length());
                HandlerInfo handlerInfo = new HandlerInfo(classInfo.loadClass());
                handlers.put(url.toLowerCase(),handlerInfo);
            }
        }
    }

    public Object getHandler(HttpServletRequest request) throws ServletException {
        String contextPath= request.getContextPath();
        String uri = request.getRequestURI().substring(contextPath.length());
        return handlers.get(uri);
    }


    public void init(String scanPackage){
        ScanResult scanResult = ScanUtils.scan(scanPackage);
        ClassInfoList classInfos = scanResult.getAllAnnotations();
        for (ClassInfo classInfo : classInfos) {
            Class<?> clz = classInfo.loadClass();
            Method[] methods = clz.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    String uri = method.getAnnotation(RequestMapping.class).value().toLowerCase();
                    HandlerBeanAdapter handlerAdapter = new HandlerBeanAdapter();
                    handlerAdapter.setClz(clz);
                    handlerAdapter.setMethod(method);
                    handlerAdapterMap.put(uri,handlerAdapter);
                }
            }
        }
    }

    public HandlerBeanAdapter getHandlerAdapter(String uri) {
        return handlerAdapterMap.get(uri);
    }
}
