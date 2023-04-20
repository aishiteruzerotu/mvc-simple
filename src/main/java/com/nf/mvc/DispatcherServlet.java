package com.nf.mvc;

import com.nf.mvc.adapters.AnnotationMethodRequestMappingHandlerAdapter;
import com.nf.mvc.adapters.HttpRequestHandlerAdapter;
import com.nf.mvc.adapters.MethodNameHandlerAdapter;
import com.nf.mvc.mapping.AnnotationMethodRequestMappingHandlerMapping;
import com.nf.mvc.mapping.NameConventionHandlerMapping;
import com.nf.mvc.util.ScanUtils;
import com.nf.mvc.view.ToStringViewResult;
import com.nf.mvc.view.VoidViewResult;
import io.github.classgraph.ScanResult;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet  {
    private static final String COMPONENT_SCAN = "scanPackage";

    protected List<HandlerMapping> handlerMappings = new ArrayList<>();
    protected List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    //region 初始化逻辑
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.initSetScanResult(config);
        this.initHandlerMappings();
        this.initHandlerAdapters();
    }

    protected void initSetScanResult(ServletConfig config) {
        String scanPackage = this.getInitParameter(config);
        ScanResult scanResult = ScanUtils.scan(scanPackage);
        this.initMvcContext(scanResult);
    }

    protected String getInitParameter(ServletConfig config) {
        String initParameter = config.getInitParameter(COMPONENT_SCAN);
        if (initParameter==null||initParameter.isEmpty()){
            throw new RuntimeException("找不到需要扫描的包");
        }
        return initParameter;
    }

    private void initMvcContext(ScanResult scanResult) {
        MvcContext.getMvcContext().config(scanResult);
    }

    private void initHandlerMappings() {
        //优先添加用户自定义的HandlerMapping
        List<HandlerMapping> customHandlerMappings = getCustomHandlerMappings();
        //mvc框架自身的HandlerMapping优先级更低，后注册
        List<HandlerMapping> defaultHandlerMappings = getDefaultHandlerMappings();

        handlerMappings.addAll(customHandlerMappings);
        handlerMappings.addAll(defaultHandlerMappings);
    }

    protected List<HandlerMapping> getCustomHandlerMappings() {
        return MvcContext.getMvcContext().getHandlerMappings();
    }

    protected List<HandlerMapping> getDefaultHandlerMappings() {
        List<HandlerMapping> mappings = new ArrayList<>();
        mappings.add(new AnnotationMethodRequestMappingHandlerMapping());
        mappings.add(new NameConventionHandlerMapping());
        return mappings;
    }

    private void initHandlerAdapters() {

        //优先添加用户自定义的HandlerAdapter
        List<HandlerAdapter> customHandlerAdapters = getCustomHandlerAdapters();
        //mvc框架自身的HandlerAdapter优先级更低，后注册
        List<HandlerAdapter> defaultHandlerAdapters = getDefaultHandlerAdapters();

        handlerAdapters.addAll(customHandlerAdapters);
        handlerAdapters.addAll(defaultHandlerAdapters);

    }

    protected List<HandlerAdapter> getCustomHandlerAdapters() {
        return MvcContext.getMvcContext().getHandlerAdapters();
    }

    protected List<HandlerAdapter> getDefaultHandlerAdapters() {
        List<HandlerAdapter> adapters = new ArrayList<>();
        adapters.add(new AnnotationMethodRequestMappingHandlerAdapter());
        adapters.add(new HttpRequestHandlerAdapter());
        adapters.add(new MethodNameHandlerAdapter());
        return adapters;
    }
    //endregion

    //region 调用逻辑
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = this.getUri(req);
        try {
            Handler handler = this.getHandlerMapping(uri);
            this.doService(req, resp, handler);
        } catch (Exception ex) {
            System.out.println("yichang dispatcher------");
        }
    }

    protected void doService(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws Exception {
        if (handler==null){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ViewResult viewResult = this.getHandlerAdapter(req, resp, handler);
        this.render(req, resp, viewResult);
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ViewResult viewResult) throws ServletException, IOException {
        viewResult.render(req,resp);
    }

    protected String getUri(HttpServletRequest req) {
        String contextPath= req.getContextPath();
        return req.getRequestURI().substring(contextPath.length());
    }

    protected Handler getHandlerMapping(String uri) throws Exception {
        for (HandlerMapping mapping : this.handlerMappings) {
            Handler handler = mapping.getHandler(uri);
            if (handler!=null) {
                return handler;
            }
        }
        return null;
    }

    protected ViewResult getHandlerAdapter(HttpServletRequest req, HttpServletResponse resp,Handler handler) throws Exception{
        for (HandlerAdapter adapter : this.handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter.handle(req,resp,handler);
            }
        }
        return null;
    }
    //endregion
}
