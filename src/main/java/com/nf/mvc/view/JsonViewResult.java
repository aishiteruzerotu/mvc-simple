package com.nf.mvc.view;

import com.nf.mvc.ViewResult;
import com.nf.mvc.util.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonViewResult extends ViewResult {
    private Object obj;

    public JsonViewResult(Object obj) {
        this.obj = obj;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String jsonText = JsonUtils.write(this.obj);
        resp.getWriter().write(jsonText);
    }
}
