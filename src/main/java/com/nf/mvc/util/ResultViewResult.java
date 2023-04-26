package com.nf.mvc.util;

import com.nf.mvc.ViewResult;
import com.nf.mvc.view.ToStringViewResult;
import com.nf.mvc.view.VoidViewResult;

public class ResultViewResult {
    static ViewResult getViewResult(Object obj) {
        ViewResult view = new VoidViewResult();
        if (obj != null) {
            if (obj instanceof ViewResult) {
                view = (ViewResult) obj;
            } else {
                view = new ToStringViewResult(obj);
            }
        }
        return view;
    }
}
