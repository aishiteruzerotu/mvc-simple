package com.nf.mvc;

import com.nf.mvc.support.HttpMethod;
import com.nf.mvc.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class CorsConfiguration {

    private List<String> allowedOrigins = new ArrayList<>();

    private List<String> allowedMethods = new ArrayList<>();

    private List<String> allowedHeaders = new ArrayList<>();

    private Boolean allowCredentials = false;

    private Long maxAge = 3600l;

    public void setAllowedOrigins(String... origins) {
        CollectionUtils.mergeArrayIntoCollection(origins, allowedOrigins);
    }

    public List<String> getAllowedOrigins() {
        return this.allowedOrigins;
    }

    public void setAllowCredentials(Boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public Boolean getAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowedHeaders(String... allowedHeaders) {
        CollectionUtils.mergeArrayIntoCollection(allowedHeaders, this.allowedHeaders);
    }

    public List<String> getAllowedHeaders() {
        return this.allowedHeaders;
    }

    public void setAllowedMethods(String... allowedMethods) {
        CollectionUtils.mergeArrayIntoCollection(allowedMethods, this.allowedMethods);
    }

    public List<String> getAllowedMethods() {
        return null;
    }

    public Long getMaxAge() {
        return this.maxAge;
    }

    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
    }

    public void applyDefaultConfiguration(){
        setAllowedOrigins("*");
        setAllowCredentials(true);
        setAllowedMethods(HttpMethod.OPTIONS.name(),HttpMethod.GET.name(),HttpMethod.POST.name(),HttpMethod.DELETE.name(),HttpMethod.PUT.name());
        setAllowedHeaders("*");
    }


}
