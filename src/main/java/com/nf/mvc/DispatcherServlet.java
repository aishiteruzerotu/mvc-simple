package com.nf.mvc;

import com.nf.mvc.util.ScanUtils;
import io.github.classgraph.ScanResult;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
public class DispatcherServlet extends HttpServlet {
    private static final String COMPONENT_SCAN = "scanPackage";

    protected List<HandlerMapping> handlerMappings = new ArrayList<>();
    protected List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    protected static MvcContext MVC_CONTEXT = MvcContext.getMvcContext();

    //region 初始化逻辑
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.initSetScanResult(config);

        this.handlerMappings = MVC_CONTEXT.getHandlerMappings();
        this.handlerAdapters = MVC_CONTEXT.getHandlerAdapters();
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
        MvcContext.getMvcContext().config(scanResult);
    }
    //endregion

    //region 调用逻辑
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.setEncoding(req,resp);
        String uri = this.getUri(req);
        try {
            Handler handler = this.getHandlerMapping(uri);
            this.doService(req, resp, handler);
        } catch (ServletException | IOException e) {
            throw e;
        } catch (Exception ex) {
            System.out.println("yichang dispatcher------");
        }
    }

    /**
     * 设置编码的方法是在service方法里面第一个调用，如果已经从req
     * 对象中获取数据了，再设置这个编码是无效
     * @param req 请求
     * @param resp 响应
     * @throws IOException 数据无效异常
     */
    protected void setEncoding(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
    }

    protected void doService(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws Exception {
        if (handler == null) {
            this.noHandlerFound(req,resp);
            return;
        }
        ViewResult viewResult = this.getHandlerAdapter(req, resp, handler);
        this.render(req, resp, viewResult);
    }

    protected void noHandlerFound(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        //我们获取容器中本来就有的默认servlet来处理静态资源
        //容器中默认servlet是有能力处理静态资源
        //默认servlet的名字，在很多容器中就是叫default，但有些容器不叫default
        //常用的tomcat，jetty这些容器中就是叫default
        req.getServletContext().getNamedDispatcher("default").forward(req,resp);
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ViewResult viewResult) throws ServletException, IOException {
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

    protected ViewResult getHandlerAdapter(HttpServletRequest req, HttpServletResponse resp, Handler handler) throws Exception {
        for (HandlerAdapter adapter : this.handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter.handle(req, resp, handler);
            }
        }
        throw new ServletException("此Handler没有对应的adapter去处理，请在DispatcherServlet中进行额外的配置");
    }
    //endregion
}
