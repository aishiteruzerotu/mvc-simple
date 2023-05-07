package com.nf.mvc;

import com.nf.mvc.support.HttpHeaders;
import com.nf.mvc.support.HttpMethod;
import com.nf.mvc.util.CorsUtils;
import com.nf.mvc.util.ScanUtils;
import com.nf.mvc.util.StringUtils;
import io.github.classgraph.ScanResult;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
public class DispatcherServlet extends HttpServlet {
    private static final String COMPONENT_SCAN = "scanPackage";

    protected List<HandlerMapping> handlerMappings = new ArrayList<>();
    protected List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    protected List<ParameterProcessor> parameterProcessors = new ArrayList<>();
    protected List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<>();

    protected final MvcContext MVC_CONTEXT = MvcContext.getMvcContext();

    protected final HandlerInterceptorMapping handlerInterceptorMapping = HandlerInterceptorMapping.getHandlerInterceptorMapping();

    protected final CorsConfiguration corsConfiguration = new CorsConfiguration();

    //region 初始化逻辑
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.initSetScanResult(config);

        //初始化配置
        this.initHandlerMappings();
        this.initHandlerAdapters();
        this.initParameterProcessors();
        this.initExceptionResolvers();

        //初始化用户配置修改
        this.initConfigurer();
    }

    protected void initConfigurer(){
        WebMvcConfigurer webMvcConfigurer = MVC_CONTEXT.getCustomWebMvcConfigurer();

        //基本框架组件配置
        this.config(handlerMappings,webMvcConfigurer::configureHandlerMapping);
        this.config(handlerAdapters,webMvcConfigurer::configureHandlerAdapter);
        this.config(parameterProcessors,webMvcConfigurer::configureArgumentResolver);
        this.config(exceptionResolvers,webMvcConfigurer::configureExceptionResolver);

        //先设定默认设置，如果用户不需要这些默认设置，可以调用clearDefaultConfiguration方法进行清除
        corsConfiguration.applyDefaultConfiguration();
        webMvcConfigurer.configureCors(corsConfiguration);
    }

    protected <T> void config(List<T> list, HandlerWebMvcConfigurer<T> handlerWebMvcConfigurer){
        for (T t : list) {
            handlerWebMvcConfigurer.config(t);
        }
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

    private void initParameterProcessors() {

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
        if (CorsUtils.isCorsRequest(req)) {
            this.processCors(req, resp, corsConfiguration);
            /*如果是预检请求需要return，以便及时响应预检请求，以便处理后续的真正请求*/
            if (CorsUtils.isPreFlightRequest(req)) {
                return;
            }
        }
        this.doService(req, resp);
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

    protected void doService(HttpServletRequest req, HttpServletResponse resp) {
        String uri = this.getUri(req);
        HandlerContext context = HandlerContext.getContext();
        context.setRequest(req).setResponse(resp);
        try {
            Handler handler = this.getHandler(uri);
            if (handler != null) {
                HandlerExecutionChain handlerExecutionChain = this.getHandlerExecutionChain(handler, uri);
                this.doDispatch(req, resp, handlerExecutionChain);
            } else {
                this.noHandlerFound(req, resp);
            }
        } catch (Throwable ex) {
            throw new RuntimeException("无法正常访问", ex);
        } finally {
            // 必须要清掉请求上下文，不然会引起堆栈溢出的问题
            context.clear();
        }
    }

    protected HandlerExecutionChain getHandlerExecutionChain(Handler handler, String uri) {
        return new HandlerExecutionChain(handler, this.handlerInterceptorMapping.getHandlerInterceptors(uri));
    }

    /**
     * 参考spring 的DefaultCorsProcessor
     * <p>在预检请求下，才会设置的项
     * <ul>
     *     <li>setAccessControlMaxAge</li>
     *     <li>setAccessControlAllowHeaders</li>
     *     <li>setAccessControlAllowMethods</li>
     * </ul>
     * </p>
     *
     * <p>
     * 不管是不是预检请求都会设置的项有
     *     <ul>
     *         <li>setAccessControlAllowOrigin</li>
     *         <li>setAccessControlAllowCredentials</li>
     *     </ul>
     * </p>
     *
     * @param req
     * @param resp
     * @param configuration
     */
    protected void processCors(HttpServletRequest req, HttpServletResponse resp, CorsConfiguration configuration) {
        String requestOrigin = req.getHeader(HttpHeaders.ORIGIN);
        String allowOrigin = configuration.checkOrigin(requestOrigin);
        if (allowOrigin == null) {
            this.rejectRequest(resp);
            return;
        }
        //设置允许跨域请求的源
        resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowOrigin);
        resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, Boolean.toString(configuration.getAllowCredentials()));

        if (HttpMethod.OPTIONS.matches(req.getMethod())) {
            // 浏览器缓存预检请求结果时间,单位:秒
            resp.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, Long.toString(configuration.getMaxAge()));
            // 允许浏览器在预检请求成功之后发送的实际请求方法名，
            // 在MDN中只说要用逗号分隔即可，https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Access-Control-Allow-Methods
            // 但其举的例子是逗号后有一个空格，spring的HttpHeaders类的toCommaDelimitedString也是这样的
            resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, StringUtils.toCommaDelimitedString(configuration.getAllowedMethods(), ", "));
            // 允许浏览器发送的请求消息头
            resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, StringUtils.toCommaDelimitedString(configuration.getAllowedHeaders(), ", "));

        }
    }

    protected void rejectRequest(HttpServletResponse response)  {
        try {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getOutputStream().write("Invalid CORS request".getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (IOException e) {
            throw new IllegalStateException("跨域处理失败");
        }
    }

    protected void doDispatch(HttpServletRequest req, HttpServletResponse resp, HandlerExecutionChain chain) throws Throwable {
        ViewResult viewResult;
        try {
            //这里返回false，直接return，结束后续流程
            if (!chain.applyPreHandle(req, resp)) {
                chain.applyPostHandle(req, resp);
                return;
            }

            viewResult = this.getViewResult(req, resp, chain.getHandler());

            chain.applyPostHandle(req, resp);
        } catch (Exception ex) {
            //这里只处理Exception，非Exception并没有处理，会继续抛出给doService处理
            //这个异常处理也只是处理了Handler整个执行层面的异常，
            // 视图渲染层面的异常是没有处理的，要处理的话可以在doService方法里处理
            viewResult = resolveException(req, resp, chain.getHandler(), ex);
        }
        render(req, resp, viewResult);
    }

    protected ViewResult resolveException(HttpServletRequest req, HttpServletResponse resp, Handler handler, Exception ex) throws Exception {
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

    protected Handler getHandler(String uri) throws Exception {
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
                + HandlerAdapter.class + " 实现类以支持该Handed的处理");
    }
    //endregion
}
