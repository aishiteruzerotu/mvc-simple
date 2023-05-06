package com.nf.mvc;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nf.mvc.annotation.Interceptor;
import com.nf.mvc.annotation.ValueConstants;

import java.util.*;

/**
 * 管理拦截器，通过拦截地址获取到对应的拦截集合
 */
public class HandlerInterceptorMapping {
    // Caffeine 动态清理缓存
    private final Cache<String, List<HandlerInterceptor>> handlerInterceptors = Caffeine.newBuilder()
            // 初始数量 20
            .initialCapacity(20)
            // 缓存最大条目数 50
            .maximumSize(50)
            .build();

    //获取客户提供的拦截器
    private final List<HandlerInterceptor> HANDLER_INTERCEPTORS = MvcContext.getMvcContext().getCustomHandlerInterceptors();

    //默认拦截地址
    private final String  DEFAULT_MAPPING_URL = "/*";

    private HandlerInterceptorMapping(){}

    //获取唯一的 HandlerInterceptorMapping 对象
    public static HandlerInterceptorMapping getHandlerInterceptorMapping(){
        return new HandlerInterceptorMapping();
    }

    /**
     * 根据映射地址，创建一个新的集合，存放该映射地址的拦截器
     * @param url 映射地址
     * @return 生成新的拦截器集合
     */
    private List<HandlerInterceptor> getHandlerInterceptorList(String url) {
        List<HandlerInterceptor> list = new ArrayList<>();
        for (HandlerInterceptor handlerInterceptor : HANDLER_INTERCEPTORS) {
            //判断拦截器是否支持拦截 当前映射 url
            if (this.isMapping(handlerInterceptor,url)){
                list.add(handlerInterceptor);
            }
        }
        return list;
    }

    /**
     * 判断该对象是否是，当前请求的拦截器
     * @param handlerInterceptor 连接器
     * @param url 请求地址
     * @return 是 返回真
     */
    private boolean isMapping(HandlerInterceptor handlerInterceptor,String url){
        Class<? extends HandlerInterceptor> clz = handlerInterceptor.getClass();
        // 没有 Interceptor 注解 ，但是 HandlerInterceptor 的实现了，则直接返回真
        if (clz.isAnnotationPresent(Interceptor.class)) {
            Interceptor annotation = clz.getAnnotation(Interceptor.class);
            String value = annotation.value();
            /*
            * 如果声明的映射范围是默认值 DEFAULT_MAPPING_URL = /*
            * 则直接返回真
             */
            if (value.equals(DEFAULT_MAPPING_URL)) {
                return true;
            }

            //判断是否以默认值结尾 DEFAULT_MAPPING_URL
            if (value.endsWith(DEFAULT_MAPPING_URL)) {
                //判断请求地址的前部分是否与获取到的值的前部分相等
                return this.deleteLastUrl(url).equals(this.getStats(value));
            }

            // 如果 value 值等于默认值，则按 注解的 values() 数组的值判断
            if (ValueConstants.DEFAULT_NONE.equals(value)){
                String[] values = annotation.values();
                for (String s : values) {
                    //判断是否以默认值结尾 DEFAULT_MAPPING_URL
                    if (s.endsWith(DEFAULT_MAPPING_URL)) {
                        //判断请求地址的前部分是否与获取到的值的前部分相等
                        return this.deleteLastUrl(url).equals(this.getStats(s));
                    }

                    //当前映射地址被匹配返回真
                    if (s.equals(url)){
                        return true;
                    }
                }
                return false;
            }

            //当前映射地址被匹配返回真
            //否则返回假
            return value.equals(url);
        }else {
            return true;
        }
    }

    /**
     * 删除 默认值
     * @param value 注解获取到的值
     * @return 被删除默认值的字符串
     */
    private String getStats(String value) {
        return (value.substring(0, value.length() - DEFAULT_MAPPING_URL.length())).toLowerCase(Locale.ROOT);
    }

    /**
     * 删除 最后的 / 映射地址
     * @param url 请求地址
     * @return 被删最后的 / 映射地址的字符串
     */
    private String deleteLastUrl(String url){
        return url.substring(0,url.lastIndexOf("/")).toLowerCase(Locale.ROOT);
    }

    /**
     * 获取拦截器
     * @param url 请求地址
     * @return 拦截器数组
     */
    public List<HandlerInterceptor> getHandlerInterceptors(String url) {
        return handlerInterceptors.get(url, k -> getHandlerInterceptorList(url));
    }
}
