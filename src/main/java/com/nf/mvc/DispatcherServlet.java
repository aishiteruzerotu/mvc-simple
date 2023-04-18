package com.nf.mvc;

import com.nf.mvc.adapters.HttpRequestHandlerAdapter;
import com.nf.mvc.mapping.NameConventionHandlerMapping;
import com.nf.mvc.util.ScanUtils;
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

    protected List<HandlerMapping> mappings = new ArrayList<>();
    protected List<HandlerAdapter> adapters = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.initSetScanResult(config);
        this.initHandlerMappingList();
        this.initHandlerAdapterList();
    }

    private void initSetScanResult(ServletConfig config) {
        String scanPackage = this.getInitParameter(config);
        ScanResult scanResult = ScanUtils.scan(scanPackage);
        MvcContext.getMvcContext().config(scanResult);
    }

    private void initHandlerMappingList() {

        this.mappings.add(new NameConventionHandlerMapping());
    }

    private void initHandlerAdapterList() {

        this.adapters.add(new HttpRequestHandlerAdapter());
    }

    private String getInitParameter(ServletConfig config) {
        String initParameter = config.getInitParameter(COMPONENT_SCAN);
        if (initParameter==null||initParameter.isEmpty()){
            throw new RuntimeException("找不到需要扫描的包");
        }
        return initParameter;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = this.getUri(req);
        try {
            Object handler = this.getHandlerMapping(uri);
            this.doService(req, resp, handler);
        } catch (Exception ex) {
            System.out.println("yichang dispatcher------");
        }
    }

    private void doService(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        if (handler==null){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ViewResult viewResult = this.getHandlerAdapter(req, resp, handler);
        viewResult.render(req, resp);
    }

    private String getUri(HttpServletRequest req) {
        String contextPath= req.getContextPath();
        return req.getRequestURI().substring(contextPath.length());
    }

    private Object getHandlerMapping(String uri) throws Exception {
        for (HandlerMapping mapping : this.mappings) {
            Object handler = mapping.getHandler(uri);
            if (handler!=null) {
                return handler;
            }
        }
        return null;
    }

    private ViewResult getHandlerAdapter(HttpServletRequest req, HttpServletResponse resp,Object handler) throws Exception{
        for (HandlerAdapter adapter : this.adapters) {
            if (adapter.supports(handler)) {
                return adapter.handle(req,resp,handler);
            }
        }
        return null;
    }
}
