package com.nf.mvc.view;

import com.nf.mvc.ViewResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ToStringViewResult extends ViewResult {
    private Object str;

    public ToStringViewResult(Object str) {
        if (str==null){
            this.str = "";
        }else {
            this.str = str;
        }
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(str.toString());
    }
}
