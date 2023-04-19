package com.nf.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class ViewResult {
    public abstract void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
