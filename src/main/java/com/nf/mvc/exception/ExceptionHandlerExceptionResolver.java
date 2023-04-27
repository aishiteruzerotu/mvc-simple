package com.nf.mvc.exception;

import com.nf.mvc.*;
import com.nf.mvc.handler.HandlerDefault;
import com.nf.mvc.util.HandlerInvokeUtils;
import com.nf.mvc.util.ScanUtils;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.nf.mvc.util.ExceptionUtils.getRootCause;

/**
 * 对含有 ExceptionHandler 注解的方法，进行全局异常处理
 */
public class ExceptionHandlerExceptionResolver implements HandlerExceptionResolver {
    private List<Handler> handlerExceptions = new ArrayList<>();

    public ExceptionHandlerExceptionResolver() {
        //获取用户设置异常捕获器
        this.scanHandleException();
        //对异常捕获器进行排序
        this.sortHandleException();
        // 设置自定义异常
        this.initDefaultHandleException();
    }

    @Override
    public ViewResult resolveException(HttpServletRequest request, HttpServletResponse response, Handler handler, Exception ex) {
        Exception exposedException = (Exception) getRootCause(ex);
        for (Handler handlerException : handlerExceptions) {
            Class<?> exceptionClass = handlerException.getMethod().getDeclaredAnnotation(ExceptionHandler.class).value();
            if (exceptionClass.isAssignableFrom(exposedException.getClass())) {
                try {
                    /*从这里可以看出，我们的全局异常处理只能有一个参数，而且必须有,参数的类型就是异常*/
                    return HandlerInvokeUtils.invoke(handlerException, exposedException);
                } catch (Exception e) {
                    /* 进入到这里就是异常处理方法本身的执行出了错，通过返回null的形式，
                     * 就继续交给下一个异常解析器去处理，下一个异常解析器处理的仍然是最开始抛出的异常，也就是这个方法被调用时传递的第四个参数的值
                     * 此处不应该抛出任何异常，因为这个类是处理异常的类，如果自身抛出异常，则违背了处理异常的本意
                     */
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 获取用户设置异常捕获器
     */
    private void scanHandleException() {
        List<Class<?>> classList = MvcContext.getMvcContext().getAllScanedClasses();

        this.setHandlerException(classList);
    }

    /**
     * 设置异常保护器
     * @param classList 扫描数据结果集
     */
    private void setHandlerException(List<Class<?>> classList) {
        for (Class<?> clz : classList) {
            Method[] methods = clz.getDeclaredMethods();
            for (Method method : methods) {
                //判断方法是否有 ExceptionHandler 注解
                if (method.isAnnotationPresent(ExceptionHandler.class)) {
                    //实例化 Handler
                    Handler exHandle = new HandlerDefault();
                    exHandle.setClz(clz);
                    exHandle.setMethod(method);
                    exHandle.setParameters(method.getParameters());
                    //添加 Handler
                    this.handlerExceptions.add(exHandle);
                }
            }
        }
    }

    /**
     * 对异常捕获器进行排序
     */
    private void sortHandleException() {
        /* RuntimeException.class.isAssignableFrom(ArithmeticException.class) 真
         * RuntimeException 是 ArithmeticException 父类
         * 所以 RuntimeException 应该排在 ArithmeticException 后面 ，所以 要返回 1 或大于 0 的任意整数
         * 当 m1 是 m2 的父类或本身，则返回 1
         * 但 m1 不是 m2 的父类或本身，所以返回 -1
         */
        this.handlerExceptions.sort((m1, m2) ->
                m1.getMethod().getDeclaredAnnotation(ExceptionHandler.class).value()
                        .isAssignableFrom(m2.getMethod().getDeclaredAnnotation(ExceptionHandler.class).value()) ? 1 : -1);
    }

    /**
     * mvc 框架提供并解析的异常
     */
    private void initDefaultHandleException() {
        List<Class<?>> lists = new ArrayList<>();

        // 扫描指定的包
        ScanResult scanResult = ScanUtils.scan("com.nf.mvc.exception");
        ClassInfoList allClasses = scanResult.getAllClasses();

        // 添加到 List 数组
        allClasses.forEach(classInfo -> lists.add(classInfo.loadClass()));

        //赋值
        this.setHandlerException(lists);
    }
}
