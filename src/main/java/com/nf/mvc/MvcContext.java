package com.nf.mvc;

import com.nf.mvc.adapters.HttpRequestHandlerAdapter;
import com.nf.mvc.adapters.MethodNameHandlerAdapter;
import com.nf.mvc.adapters.MethodRequestMappingHandlerAdapter;
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
    private List<ParameterProcessor> argumentResolvers = new ArrayList<>();
    private List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<>();

    // 扫描到的所有的类
    private List<Class<?>> allScanedClasses = new ArrayList<>();

    //用户定义实现类
    private List<HandlerMapping> customHandlerMappings = new ArrayList<>();
    private List<HandlerAdapter> customHandlerAdapters = new ArrayList<>();
    private List<ParameterProcessor> customArgumentResolvers = new ArrayList<>();
    private List<HandlerExceptionResolver> customExceptionResolvers = new ArrayList<>();

    //自身框架提供实现类
    private List<HandlerMapping> defaultHandlerMappings = new ArrayList<>();
    private List<HandlerAdapter> defaultHandlerAdapters = new ArrayList<>();
    private List<ParameterProcessor> defaultArgumentResolvers = new ArrayList<>();
    private List<HandlerExceptionResolver> defaultExceptionResolvers = new ArrayList<>();
    //endregion

    private MvcContext(){}

    //region 初始化
    public static MvcContext getMvcContext(){
        return instance;
    }

    /**
     * 设置成public修饰符，框架使用者是可以直接修改这个扫描
     * 影响就不是很好，所以就改成default修饰符，只能在本包
     * 或子包中访问，基本上就是mvc框架内可以访问
     *
     * 我设计的mvc框架，是有以下几个扩展点
     * DispatcherServlet
     * HandlerMapping
     * HandlerAdapter
     * ParameterProcessor
     * HandlerExceptionResolver
     * ViewResult
     *
     * 目前扫描的是各种各样，有HandlerMapping，也有HandlerAdapter
     * 也有Handler
     * 因为一般不会写一个类，实现多个接口，所以这种多个if写法问题不大
     *
     * @param scanResult
     */
    void config(ScanResult scanResult) {

        this.scanResult =scanResult;
        ClassInfoList allClasses = scanResult.getAllClasses();
        for (ClassInfo classInfo : allClasses) {
            Class<?> scanedClass = classInfo.loadClass();

            this.setList(HandlerMapping.class,scanedClass,this.customHandlerMappings);
            this.setList(HandlerAdapter.class,scanedClass,this.customHandlerAdapters);
            this.setList(ParameterProcessor.class,scanedClass,this.customArgumentResolvers);
            this.setList(HandlerExceptionResolver.class,scanedClass,this.customExceptionResolvers);

            allScanedClasses.add(scanedClass);
        }

        //排序操作
        this.customHandlerMappings.sort(ORDER_COMPARATOR);
        this.customHandlerAdapters.sort(ORDER_COMPARATOR);
        this.customArgumentResolvers.sort(ORDER_COMPARATOR);
        this.customExceptionResolvers.sort(ORDER_COMPARATOR);

        // 配置用户实现类
        this.handlerMappings.addAll(this.customHandlerMappings);
        this.handlerAdapters.addAll(this.customHandlerAdapters);
        this.argumentResolvers.addAll(this.customArgumentResolvers);
        this.exceptionResolvers.addAll(this.customExceptionResolvers);

        // 配置默认实现类
        this.handlerMappings.addAll(this.getDefaultHandlerMappings());
        this.handlerAdapters.addAll(this.getDefaultHandlerAdapters());
        this.argumentResolvers.addAll(this.getDefaultArgumentResolvers());
        this.exceptionResolvers.addAll(this.getDefaultExceptionResolvers());
    }

    private <T> void setList(Class<? extends T> clz,Class<?> scanedClass,List<T> arr){
        if (clz.isAssignableFrom(scanedClass)) {
            T exceptionResolver = (T) ReflectionUtils.newInstance(scanedClass);
            arr.add(exceptionResolver);
        }
    }

    ScanResult getScanResult(){
        return  this.scanResult;
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

    public List<HandlerAdapter> getHandlerAdapters(){
        return Collections.unmodifiableList(this.handlerAdapters);
    }

    public List<ParameterProcessor> getArgumentResolvers(){
        return Collections.unmodifiableList(this.argumentResolvers);
    }

    public List<HandlerExceptionResolver> getExceptionResolvers(){
        return Collections.unmodifiableList(this.exceptionResolvers);
    }

    public List<Class<?>> getAllScanedClasses(){
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

    public List<ParameterProcessor> getCustomArgumentResolvers() {
        return Collections.unmodifiableList(this.customArgumentResolvers);
    }

    public List<HandlerExceptionResolver> getCustomExceptionResolvers() {
        return Collections.unmodifiableList(this.customExceptionResolvers);
    }
    //endregion

    //region 返回默认实现类
    public List<HandlerMapping> getDefaultHandlerMappings() {
        this.defaultHandlerMappings.add(new RequestControllerHandlerMapping());
        this.defaultHandlerMappings.add(new MethodRequestMappingHandlerMapping());
        this.defaultHandlerMappings.add(new NameConventionHandlerMapping());
        return Collections.unmodifiableList(this.defaultHandlerMappings);
    }

    public List<HandlerAdapter> getDefaultHandlerAdapters() {
        this.defaultHandlerAdapters.add(new MethodRequestMappingHandlerAdapter());
        this.defaultHandlerAdapters.add(new HttpRequestHandlerAdapter());
        this.defaultHandlerAdapters.add(new MethodNameHandlerAdapter());
        return Collections.unmodifiableList(this.defaultHandlerAdapters);
    }

    public List<ParameterProcessor> getDefaultArgumentResolvers() {
        ScanResult scanResult = ScanUtils.scan("com.nf.mvc.parameter");
        ClassInfoList allClasses = scanResult.getAllClasses();
        for (ClassInfo classInfo : allClasses) {
            Class<?> scanedClass = classInfo.loadClass();
            if (scanedClass.isAssignableFrom(scanedClass)) {
                ParameterProcessor exceptionResolver = (ParameterProcessor) ReflectionUtils.newInstance(scanedClass);
                this.defaultArgumentResolvers.add(exceptionResolver);
            }
        }
        this.defaultArgumentResolvers.sort(new OrderComparator<>());
        return Collections.unmodifiableList(this.defaultArgumentResolvers);
    }

    public List<HandlerExceptionResolver> getDefaultExceptionResolvers() {
        return Collections.unmodifiableList(this.defaultExceptionResolvers);
    }
    //endregion
}

