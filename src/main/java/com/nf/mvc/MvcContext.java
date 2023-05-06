package com.nf.mvc;

import com.nf.mvc.adapters.HttpRequestHandlerAdapter;
import com.nf.mvc.adapters.MethodNameHandlerAdapter;
import com.nf.mvc.adapters.MethodRequestMappingHandlerAdapter;
import com.nf.mvc.exception.ExceptionHandlerExceptionResolver;
import com.nf.mvc.exception.PrintStackTraceHandlerExceptionResolver;
import com.nf.mvc.mapping.MethodRequestMappingHandlerMapping;
import com.nf.mvc.mapping.NameConventionHandlerMapping;
import com.nf.mvc.mapping.RequestControllerHandlerMapping;
import com.nf.mvc.support.OrderComparator;
import com.nf.mvc.util.ReflectionUtils;
import com.nf.mvc.util.ScanUtils;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MvcContext {

    private static final MvcContext instance = new MvcContext();

    private static final OrderComparator ORDER_COMPARATOR = new OrderComparator<>();

    private ScanResult scanResult;

    //region 声明
    //声明所有配置
    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private List<ParameterProcessor> parameterProcessors = new ArrayList<>();
    private List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<>();

    // 扫描到的所有的类
    private List<Class<?>> allScanedClasses = new ArrayList<>();

    //用户定义实现类
    private List<HandlerMapping> customHandlerMappings = new ArrayList<>();
    private List<HandlerAdapter> customHandlerAdapters = new ArrayList<>();
    private List<ParameterProcessor> customParameterProcessors = new ArrayList<>();
    private List<HandlerExceptionResolver> customExceptionResolvers = new ArrayList<>();
    private List<HandlerInterceptor> customHandlerInterceptors = new ArrayList<>();

    //自身框架提供实现类
    private List<HandlerMapping> defaultHandlerMappings = new ArrayList<>();
    private List<HandlerAdapter> defaultHandlerAdapters = new ArrayList<>();
    private List<ParameterProcessor> defaultParameterProcessors = new ArrayList<>();
    private List<HandlerExceptionResolver> defaultExceptionResolvers = new ArrayList<>();
    //endregion

    private MvcContext() {
    }

    //region 初始化
    public static MvcContext getMvcContext() {
        return instance;
    }

    /**
     * 设置成public修饰符，框架使用者是可以直接修改这个扫描
     * 影响就不是很好，所以就改成default修饰符，只能在本包
     * 或子包中访问，基本上就是mvc框架内可以访问
     * <p>
     * 我设计的mvc框架，是有以下几个扩展点
     * DispatcherServlet
     * HandlerMapping
     * HandlerAdapter
     * ParameterProcessor
     * HandlerExceptionResolver
     * ViewResult
     * <p>
     * 目前扫描的是各种各样，有HandlerMapping，也有HandlerAdapter
     * 也有Handler
     * 因为一般不会写一个类，实现多个接口，所以这种多个if写法问题不大
     *
     * @param scanResult
     */
    void config(ScanResult scanResult) {

        this.scanResult = scanResult;
        ClassInfoList allClasses = scanResult.getAllClasses();
        for (ClassInfo classInfo : allClasses) {
            Class<?> scanedClass = classInfo.loadClass();

            setList(scanedClass);

            allScanedClasses.add(scanedClass);
        }

        //排序操作
        this.customHandlerMappings.sort(ORDER_COMPARATOR);
        this.customHandlerAdapters.sort(ORDER_COMPARATOR);
        this.customParameterProcessors.sort(ORDER_COMPARATOR);
        this.customExceptionResolvers.sort(ORDER_COMPARATOR);
        this.customHandlerInterceptors.sort(ORDER_COMPARATOR);
    }

    private void setList(Class<?> scanedClass) {
        this.setList(HandlerMapping.class, scanedClass, this.customHandlerMappings);
        this.setList(HandlerAdapter.class, scanedClass, this.customHandlerAdapters);
        this.setList(ParameterProcessor.class, scanedClass, this.customParameterProcessors);
        this.setList(HandlerExceptionResolver.class, scanedClass, this.customExceptionResolvers);
        this.setList(HandlerInterceptor.class, scanedClass, this.customHandlerInterceptors);
    }

    private <T> void setList(Class<? extends T> clz, Class<?> scanedClass, List<T> arr) {
        if (clz.isAssignableFrom(scanedClass)) {
            T exceptionResolver = (T) ReflectionUtils.newInstance(scanedClass);
            arr.add(exceptionResolver);
        }
    }

    ScanResult getScanResult() {
        return this.scanResult;
    }

    void setHandlerMappings(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    void setHandlerAdapters(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    void setParameterProcessors(List<ParameterProcessor> parameterProcessors) {
        this.parameterProcessors = parameterProcessors;
    }

    void setExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        this.exceptionResolvers = exceptionResolvers;
    }

    void init() {
        this.defaultHandlerMappings.add(new RequestControllerHandlerMapping());
        this.defaultHandlerMappings.add(new NameConventionHandlerMapping());
        this.defaultHandlerMappings.add(new MethodRequestMappingHandlerMapping());

        this.defaultHandlerAdapters.add(new MethodRequestMappingHandlerAdapter());
        this.defaultHandlerAdapters.add(new HttpRequestHandlerAdapter());
        this.defaultHandlerAdapters.add(new MethodNameHandlerAdapter());

        ScanResult scanResult = ScanUtils.scan("com.nf.mvc.parameter.reference");
        ClassInfoList allClasses = scanResult.getAllClasses();
        for (ClassInfo classInfo : allClasses) {
            Class<?> scanedClass = classInfo.loadClass();
            ParameterProcessor exceptionResolver = (ParameterProcessor) ReflectionUtils.newInstance(scanedClass);
            this.defaultParameterProcessors.add(exceptionResolver);
        }
        this.defaultParameterProcessors.sort(new OrderComparator<>());
        this.defaultExceptionResolvers.add(new ExceptionHandlerExceptionResolver());

        this.defaultExceptionResolvers.add(new ExceptionHandlerExceptionResolver());
        this.defaultExceptionResolvers.add(new PrintStackTraceHandlerExceptionResolver());
    }

    //endregion

    //region 返回所有的实现类

    /**
     * 因为我们解析之后，结果就是固定的，如果直接返回List
     * 用户是可以改这个集合里面的内容，所以返回一个只读集合
     *
     * @return
     */
    public List<HandlerMapping> getHandlerMappings() {
        return Collections.unmodifiableList(this.handlerMappings);
    }

    public List<HandlerAdapter> getHandlerAdapters() {
        return Collections.unmodifiableList(this.handlerAdapters);
    }

    public List<ParameterProcessor> getParameterProcessors() {
        return Collections.unmodifiableList(this.parameterProcessors);
    }

    public List<HandlerExceptionResolver> getExceptionResolvers() {
        return Collections.unmodifiableList(this.exceptionResolvers);
    }

    public List<Class<?>> getAllScanedClasses() {
        return Collections.unmodifiableList(this.allScanedClasses);
    }
    //endregion

    //region 返回用户提供的实现类
    public List<HandlerMapping> getCustomHandlerMappings() {
        return Collections.unmodifiableList(this.customHandlerMappings);
    }

    public List<HandlerAdapter> getCustomHandlerAdapters() {
        return Collections.unmodifiableList(this.customHandlerAdapters);
    }

    public List<ParameterProcessor> getCustomParameterProcessors() {
        return Collections.unmodifiableList(this.customParameterProcessors);
    }

    public List<HandlerExceptionResolver> getCustomExceptionResolvers() {
        return Collections.unmodifiableList(this.customExceptionResolvers);
    }

    public List<HandlerInterceptor> getCustomHandlerInterceptors() {
        return Collections.unmodifiableList(this.customHandlerInterceptors);
    }

    //endregion

    //region 返回默认实现类
    public List<HandlerMapping> getDefaultHandlerMappings() {
        return Collections.unmodifiableList(this.defaultHandlerMappings);
    }

    public List<HandlerAdapter> getDefaultHandlerAdapters() {
        return Collections.unmodifiableList(this.defaultHandlerAdapters);
    }

    public List<ParameterProcessor> getDefaultParameterProcessors() {
        return Collections.unmodifiableList(this.defaultParameterProcessors);
    }

    public List<HandlerExceptionResolver> getDefaultExceptionResolvers() {
        return Collections.unmodifiableList(this.defaultExceptionResolvers);
    }
    //endregion
}

