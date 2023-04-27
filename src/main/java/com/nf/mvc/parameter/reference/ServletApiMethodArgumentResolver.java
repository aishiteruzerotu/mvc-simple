package com.nf.mvc.parameter.reference;

import com.nf.mvc.Handler;
import com.nf.mvc.HandlerContext;
import com.nf.mvc.ParameterProcessor;
import com.nf.mvc.support.Order;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Parameter;

@Order(3)
public class ServletApiMethodArgumentResolver implements ParameterProcessor {
    protected Parameter parameter;

    protected Class<?> parameterType;

    @Override
    public boolean supports(Parameter parameter) {
        this.parameter = parameter;
        this.parameterType = parameter.getType();

        return HttpServletRequest.class.isAssignableFrom(this.parameterType)||
                HttpServletResponse.class.isAssignableFrom(this.parameterType)||
                HttpSession.class.isAssignableFrom(this.parameterType)||
                ServletContext.class.isAssignableFrom(this.parameterType);
    }

    /**
     * 参数解析，是从请求中获取数据的，所以方法设计没有response对象是合理的
     * 但这样给我们带来一个解析的问题，无法获取到response对象
     * request与response对象必须来自于DispatcherServlet的service
     */
    @Override
    public Object processor(Handler handler, HttpServletRequest request)  throws IOException {
        Class<?> paramType = this.parameterType;
        HandlerContext context = HandlerContext.getContext();
        if (HttpServletRequest.class.isAssignableFrom(paramType)) {
            return request;
        }
        if (HttpServletResponse.class.isAssignableFrom(paramType)) {
            return context.getResponse();
        }
        if (HttpSession.class.isAssignableFrom(paramType)) {
           // return request.getSession();//这样可以
            return context.getSession();
        }
        if (ServletContext.class.isAssignableFrom(paramType)) {
            return request.getServletContext();
        }
        return null;
    }
}
