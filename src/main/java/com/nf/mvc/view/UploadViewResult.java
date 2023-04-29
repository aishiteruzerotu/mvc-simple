package com.nf.mvc.view;

import com.nf.mvc.ViewResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

public class UploadViewResult extends ViewResult {
    String filePath;

    public UploadViewResult(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        // TODO: 文件上传
        Part part = req.getPart("picture");
        part.write(filePath);
        resp.getWriter().write("文件地址"+ filePath);
    }
}
