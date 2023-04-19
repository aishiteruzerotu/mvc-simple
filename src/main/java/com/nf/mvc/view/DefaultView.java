package com.nf.mvc.view;

import com.nf.mvc.ViewResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultView extends ViewResult {
    private Object str;

    public DefaultView(Object str) {
        if (str==null){
            this.str = "";
        }else {
            this.str = str;
        }
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
        resp.getWriter().write(str.toString());
    }
}
