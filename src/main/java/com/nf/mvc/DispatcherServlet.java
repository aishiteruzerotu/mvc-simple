package com.nf.mvc;

import com.nf.mvc.exception.ExceptionHandlerExceptionResolver;
import com.nf.mvc.exception.PrintStackTraceHandlerExceptionResolver;
import com.nf.mvc.support.HttpHeaders;
import com.nf.mvc.support.HttpMethod;
import com.nf.mvc.util.CorsUtils;
import com.nf.mvc.util.ScanUtils;
import io.github.classgraph.ScanResult;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
public class DispatcherServlet extends HttpServlet {
    private static final String COMPONENT_SCAN = "scanPackage";

    protected List<HandlerMapping> handlerMappings = new ArrayList<>();
    protected List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    protected List<ParameterProcessor> parameterProcessors = new ArrayList<>();
    private List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<>();

    protected static MvcContext MVC_CONTEXT = MvcContext.getMvcContext();

    //region 初始化逻辑
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.initSetScanResult(config);

        this.initHandlerMappings();
        this.initHandlerAdapters();
        this.initParameterProcessors();
        this.initExceptionResolvers();
    }

    protected void initSetScanResult(ServletConfig config) {
        String scanPackage = this.getInitParameter(config);
        ScanResult scanResult = ScanUtils.scan(scanPackage);
        this.initMvcContext(scanResult);
    }

    protected String getInitParameter(ServletConfig config) {
        String initParameter = config.getInitParameter(COMPONENT_SCAN);
        if (initParameter == null || initParameter.isEmpty()) {
            throw new RuntimeException("找不到需要扫描的包");
        }
        return initParameter;
    }

    private void initMvcContext(ScanResult scanResult) {
        MVC_CONTEXT.config(scanResult);
        MVC_CONTEXT.init();
    }

    private void initHandlerMappings() {
        //优先添加用户自定义的HandlerMapping
        List<HandlerMapping> customHandlerMappings = this.getCustomHandlerMappings();
        //mvc框架自身的HandlerMapping优先级更低，后注册
        List<HandlerMapping> defaultHandlerMappings = this.getDefaultHandlerMappings();

        this.handlerMappings.addAll(customHandlerMappings);
        this.handlerMappings.addAll(defaultHandlerMappings);
        MVC_CONTEXT.setHandlerMappings(this.handlerMappings);
    }

    protected List<HandlerMapping> getDefaultHandlerMappings() {
        return MVC_CONTEXT.getDefaultHandlerMappings();
    }

    protected List<HandlerMapping> getCustomHandlerMappings() {
        return MVC_CONTEXT.getCustomHandlerMappings();
    }

    private void initHandlerAdapters() {
        //优先添加用户自定义的HandlerAdapter
        List<HandlerAdapter> customHandlerAdapters = this.getCustomHandlerAdapters();
        //mvc框架自身的HandlerAdapter优先级更低，后注册
        List<HandlerAdapter> defaultHandlerAdapters = this.getDefaultHandlerAdapters();

        this.handlerAdapters.addAll(customHandlerAdapters);
        this.handlerAdapters.addAll(defaultHandlerAdapters);
        MVC_CONTEXT.setHandlerAdapters(this.handlerAdapters);
    }

    private List<HandlerAdapter> getDefaultHandlerAdapters() {
        return MVC_CONTEXT.getDefaultHandlerAdapters();
    }

    private List<HandlerAdapter> getCustomHandlerAdapters() {
        return MVC_CONTEXT.getCustomHandlerAdapters();
    }

    private void initParameterProcessors(){

        List<ParameterProcessor> customArgumentResolvers = this.getCustomParameterProcessors();

        List<ParameterProcessor> defaultArgumentResolvers = this.getDefaultParameterProcessors();

        this.parameterProcessors.addAll(customArgumentResolvers);
        this.parameterProcessors.addAll(defaultArgumentResolvers);
        //把定制+默认的所有HandlerMapping组件添加到上下文中
        MVC_CONTEXT.setParameterProcessors(this.parameterProcessors);
    }

    protected List<ParameterProcessor> getDefaultParameterProcessors() {
        return MVC_CONTEXT.getDefaultParameterProcessors();
    }

    protected List<ParameterProcessor> getCustomParameterProcessors() {
        return MVC_CONTEXT.getCustomParameterProcessors();
    }

    private void initExceptionResolvers() {
        //优先添加用户自定义的 HandlerExceptionResolver
        List<HandlerExceptionResolver> customExceptionResolvers = this.getCustomExceptionResolvers();
        //mvc框架自身的 HandlerExceptionResolver 优先级更低，后注册
        List<HandlerExceptionResolver> defaultExceptionResolvers = this.getDefaultExceptionResolvers();

        exceptionResolvers.addAll(customExceptionResolvers);
        exceptionResolvers.addAll(defaultExceptionResolvers);
        //把定制+默认的所有 HandlerExceptionResolver 组件添加到上下文中
        MVC_CONTEXT.setExceptionResolvers(exceptionResolvers);
    }

    protected List<HandlerExceptionResolver> getCustomExceptionResolvers() {
        return MVC_CONTEXT.getCustomExceptionResolvers();
    }

    protected List<HandlerExceptionResolver> getDefaultExceptionResolvers() {
        return MVC_CONTEXT.getDefaultExceptionResolvers();
    }

    //endregion

    //region 调用逻辑
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.setEncoding(req, resp);
        processCors(req, resp, new CorsConfiguration());
        /*如果是预检请求需要return，以便及时响应预检请求，以便处理后续的真正请求*/
        if (CorsUtils.isPreFlightRequest(req)) {
            return;
        }
        this.doService(req,resp);
    }

    /**
     * 设置编码的方法是在service方法里面第一个调用，如果已经从req
     * 对象中获取数据了，再设置这个编码是无效
     *
     * @param req  请求
     * @param resp 响应
     * @throws IOException 数据无效异常
     */
    protected void setEncoding(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
    }

    protected void doService(HttpServletRequest req, HttpServletResponse resp){
        String uri = this.getUri(req);
        HandlerContext context = HandlerContext.getContext();
        context.setRequest(req).setResponse(resp);
        try {
            Handler handler = this.getHandlerMapping(uri);
            if (handler != null) {
                this.doDispatch(req, resp, handler);
            }else {
                this.noHandlerFound(req, resp);
            }
        }  catch (Throwable ex) {
            throw new RuntimeException("无法正常访问",ex);
        }finally {
            // 必须要清掉请求上下文，不然会引起堆栈溢出的问题
            context.clear();
        }
    }

    /**
     * 这里没有用到cors配置，纯粹的直接允许跨域请求
     * @param req
     * @param resp
     * @param configuration
     */
    protected void processCors(HttpServletRequest req, HttpServletResponse resp, CorsConfiguration configuration) {
        // 解决跨域请求问题
        String origin = req.getHeader(HttpHeaders.ORIGIN);

        // 允许指定域访问跨域资源
        resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        // 允许客户端携带跨域cookie，此时origin值不能为“*”，只能为指定单一域名
        resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        if (HttpMethod.OPTIONS.matches(req.getMethod())) {
            String allowMethod = req.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD);
            String allowHeaders = req.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
            // 浏览器缓存预检请求结果时间,单位:秒
            resp.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "86400");
            // 允许浏览器在预检请求成功之后发送的实际请求方法名
            resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, allowMethod);
            // 允许浏览器发送的请求消息头
            resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, allowHeaders);
        }
    }

    protected void doDispatch(HttpServletRequest req, HttpServletResponse resp,Handler handler) throws Throwable {
        ViewResult viewResult;
        try {
//            HandlerAdapter adapter = getHandlerAdapter(handler);
            viewResult = this.getViewResult(req, resp, handler);
        } catch (Exception ex) {
            //这里只处理Exception，非Exception并没有处理，会继续抛出给doService处理
            //这个异常处理也只是处理了Handler整个执行层面的异常，
            // 视图渲染层面的异常是没有处理的，要处理的话可以在doService方法里处理
            viewResult = this.resolveException(req, resp, handler, ex);
        }
        this.render(req, resp, viewResult);
    }

    protected ViewResult resolveException(HttpServletRequest req, HttpServletResponse resp, Handler handler, Exception ex) throws Exception{
        for (HandlerExceptionResolver exceptionResolver : exceptionResolvers) {
            ViewResult result = exceptionResolver.resolveException(req, resp, handler, ex);
            if (result != null) {
                return result;
            }
        }
        /*表示没有一个异常解析器可以处理异常，那么就应该把异常继续抛出*/
        throw ex;
    }

    protected void noHandlerFound(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        //我们获取容器中本来就有的默认servlet来处理静态资源
        //容器中默认servlet是有能力处理静态资源
        //默认servlet的名字，在很多容器中就是叫default，但有些容器不叫default
        //常用的tomcat，jetty这些容器中就是叫default
        req.getServletContext().getNamedDispatcher("default").forward(req, resp);
    }

    protected void render(HttpServletRequest req, HttpServletResponse resp, ViewResult viewResult) throws ServletException, IOException {
        viewResult.render(req, resp);
    }

    protected String getUri(HttpServletRequest req) {
        String contextPath = req.getContextPath();
        return req.getRequestURI().substring(contextPath.length());
    }

    protected Handler getHandlerMapping(String uri) throws Exception {
        for (HandlerMapping mapping : this.handlerMappings) {
            Handler handler = mapping.getHandler(uri);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    protected ViewResult getViewResult(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws Exception {
        for (HandlerAdapter adapter : this.handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter.handle(req, resp, handler);
            }
        }
        throw new ServletException("此Handler没有对应的adapter去处理，请在DispatcherServlet中进行额外的配置，或者添加 "
                +HandlerAdapter.class+" 实现类以支持该Handed的处理");
    }
    //endregion
}
