package com.nf.mvc.view;

import com.nf.mvc.ViewResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HtmlViewResult extends ViewResult {
    private String html;

    public HtmlViewResult(String html) {
        this.html = html;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(html);
    }
}
