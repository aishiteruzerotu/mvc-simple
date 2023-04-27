package com.nf.mvc.exception;

import com.nf.mvc.Handler;
import com.nf.mvc.HandlerExceptionResolver;
import com.nf.mvc.MvcContext;
import com.nf.mvc.ViewResult;
import com.nf.mvc.handler.HandlerDefault;
import com.nf.mvc.util.HandlerInvokeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.nf.mvc.util.ExceptionUtils.getRootCause;

/**
 * 对用户含有 ExceptionHandler 注解的方法，进行全局异常处理
 */
public class ExceptionHandlerExceptionResolver implements HandlerExceptionResolver {
    private List<Handler> exHandles = new ArrayList<>();

    public ExceptionHandlerExceptionResolver() {
        //获取用户设置异常捕获器
        scanHandleExMethods();
        //对异常捕获器进行排序
        sortExHandleMethods();
    }

    @Override
    public ViewResult resolveException(HttpServletRequest request, HttpServletResponse response, Handler handler, Exception ex) {
        Exception exposedException = (Exception) getRootCause(ex);
        for (Handler exHandler : exHandles) {
            Class<?> exceptionClass = exHandler.getMethod().getDeclaredAnnotation(ExceptionHandler.class).value();
            if (exceptionClass.isAssignableFrom(exposedException.getClass())) {
                try {
                    /*从这里可以看出，我们的全局异常处理只能有一个参数，而且必须有,参数的类型就是异常*/
                    return HandlerInvokeUtils.invoke(exHandler,exposedException);
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
    private void scanHandleExMethods() {
        List<Class<?>> classList = MvcContext.getMvcContext().getAllScanedClasses();

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
                    this.exHandles.add(exHandle);
                }
            }
        }
    }

    /**
     * 对异常捕获器进行排序
     */
    private void sortExHandleMethods() {
        /* RuntimeException.class.isAssignableFrom(ArithmeticException.class) 真
         * RuntimeException 是 ArithmeticException 父类
         * 所以 RuntimeException 应该排在 ArithmeticException 后面 ，所以 要返回 1 或大于 0 的任意整数
         * 当 m1 是 m2 的父类或本身，则返回 1
         * 但 m1 不是 m2 的父类或本身，所以返回 -1
         */
        this.exHandles.sort((m1, m2) ->
                m1.getMethod().getDeclaredAnnotation(ExceptionHandler.class).value()
                        .isAssignableFrom(m2.getMethod().getDeclaredAnnotation(ExceptionHandler.class).value()) ? 1 : -1);
    }
}
