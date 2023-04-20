package com.nf.mvc.view;

import com.nf.mvc.ViewResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PlainViewResult extends ViewResult {
    private String text;

    public PlainViewResult(String text) {
        this.text = text;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().write(text);
    }
}
