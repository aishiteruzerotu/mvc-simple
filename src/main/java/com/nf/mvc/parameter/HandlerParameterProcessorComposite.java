package com.nf.mvc.parameter;

import com.nf.mvc.MethodParameter;
import com.nf.mvc.ParameterProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerParameterProcessorComposite implements ParameterProcessor {

    private final List<ParameterProcessor> ParameterProcessors = new ArrayList<>();

    private final Map<MethodParameter, ParameterProcessor> resolverCache = new ConcurrentHashMap<>(256);

    public HandlerParameterProcessorComposite addResolver(ParameterProcessor resolver) {
        ParameterProcessors.add(resolver);
        return this;
    }

    public HandlerParameterProcessorComposite addResolvers(ParameterProcessor... resolvers) {
        if (resolvers != null) {
            Collections.addAll(this.ParameterProcessors, resolvers);
        }
        return this;
    }

    public List<ParameterProcessor> getResolvers() {
        return Collections.unmodifiableList(this.ParameterProcessors);
    }

    public void clear() {
        this.ParameterProcessors.clear();
    }

    public HandlerParameterProcessorComposite addResolvers(List<ParameterProcessor> resolvers) {
        if (resolvers != null) {
            this.ParameterProcessors.addAll(resolvers);
        }
        return this;
    }

    @Override
    public boolean supports(MethodParameter parameter) {
        return getParameterProcessor(parameter) != null;
    }

    @Override
    public Object processor(MethodParameter parameter, HttpServletRequest request) throws ServletException, IOException {
        ParameterProcessor resolver = getParameterProcessor(parameter);
        if (resolver == null) {
            throw new IllegalArgumentException("不支持的参数类型 [" +
                    parameter.getParamType() + "]. supportsParameter 方法应该先调用");
        }
        return resolver.processor(parameter, request);

    }

    private ParameterProcessor getParameterProcessor(MethodParameter parameter) {
        ParameterProcessor result = resolverCache.get(parameter);
        if (result == null) {
            for (ParameterProcessor ParameterProcessor : ParameterProcessors) {
                if (ParameterProcessor.supports(parameter)) {
                    result = ParameterProcessor;
                    resolverCache.put(parameter, ParameterProcessor);
                    break;
                }
            }
        }
        return result;
    }
}
