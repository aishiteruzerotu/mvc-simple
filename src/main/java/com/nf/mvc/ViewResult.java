package com.nf.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ViewResult {
    public abstract void render(HttpServletRequest req, HttpServletResponse resp);
}
