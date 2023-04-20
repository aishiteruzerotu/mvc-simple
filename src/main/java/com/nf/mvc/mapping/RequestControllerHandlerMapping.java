package com.nf.mvc.mapping;

import com.nf.mvc.annotation.RequestController;
import com.nf.mvc.annotation.RequestMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Locale;

public class RequestControllerHandlerMapping extends AbstractHandlerMapping {
    public RequestControllerHandlerMapping() {
    }

    protected String getUri(Class<?> clz, Method method) {
        String clzUri = getUrl(clz);
        String methodUri = method.getAnnotation(RequestMapping.class).value();
        return (clzUri + methodUri).toLowerCase(Locale.ROOT);
    }

    protected String getUrl(AnnotatedElement element) {
        String uri = "";
        if (element.isAnnotationPresent(RequestController.class)) {
            RequestController controller = element.getDeclaredAnnotation(RequestController.class);
            uri = controller.value();
        }
        return uri;
    }
}
