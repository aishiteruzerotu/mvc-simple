package com.nf.mvc.view;

import com.nf.mvc.ViewResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VoidView extends ViewResult {
    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
