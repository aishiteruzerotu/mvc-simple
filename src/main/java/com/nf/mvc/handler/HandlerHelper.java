package com.nf.mvc.handler;

import com.nf.mvc.view.*;

public class HandlerHelper {
    public static JsonViewResult json(Object obj) {
        return new JsonViewResult(obj);
    }

    public static PlainViewResult plain(String text) {
        return new PlainViewResult(text);
    }

    public static HtmlViewResult html(String html) {
        return new HtmlViewResult(html);
    }

    public static ForwardViewResult forward(String url) {
        return new ForwardViewResult(url);
    }

    public static RedirectViewResult redirect(String url) {
        return new RedirectViewResult(url);
    }
}
