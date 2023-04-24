package com.nf.mvc.view;

import com.nf.mvc.ViewResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RedirectViewResult extends ViewResult {

    private String url;
    private Map<String, Object> model;

    public RedirectViewResult(String url) {
        this(url, new HashMap<>());
    }

    public RedirectViewResult(String url, Map<String, Object> model) {
        this.url = url;
        this.model = model;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.url += initModel();
        resp.sendRedirect(url);
    }

    private String initModel() {
        if (model.size() == 0) return "";

        StringBuilder builder = new StringBuilder("?");
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append("&");
        }

        //去 & 连接符
        if (builder.length() >= 2) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }


}
