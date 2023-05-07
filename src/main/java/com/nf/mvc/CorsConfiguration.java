package com.nf.mvc;

import com.nf.mvc.support.HttpMethod;
import com.nf.mvc.util.CollectionUtils;
import com.nf.mvc.util.ObjectUtils;
import com.nf.mvc.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CorsConfiguration {

    public static final String ALL = "*";

    private List<String> allowedOrigins = new ArrayList<>();

    private List<String> allowedMethods = new ArrayList<>();

    private List<String> allowedHeaders = new ArrayList<>();

    private Boolean allowCredentials = false;

    private Long maxAge = 3600l;

    public CorsConfiguration setAllowedOrigins(String... origins) {
        CollectionUtils.mergeArrayIntoCollection(origins, allowedOrigins);
        return this;
    }

    public List<String> getAllowedOrigins() {
        return this.allowedOrigins;
    }

    public CorsConfiguration setAllowCredentials(Boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
        return this;
    }

    public Boolean getAllowCredentials() {
        return allowCredentials;
    }

    public CorsConfiguration setAllowedHeaders(String... allowedHeaders) {
        CollectionUtils.mergeArrayIntoCollection(allowedHeaders, this.allowedHeaders);
        return this;
    }

    public List<String> getAllowedHeaders() {
        return this.allowedHeaders;
    }

    public CorsConfiguration setAllowedMethods(HttpMethod... allowedMethods) {
        for (HttpMethod allowedMethod : allowedMethods) {
            this.allowedMethods.add(allowedMethod.name());
        }
        return this;
    }

    public List<String> getAllowedMethods() {
        return this.allowedMethods;
    }

    public Long getMaxAge() {
        return this.maxAge;
    }

    public CorsConfiguration setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    /**
     * 应用默认的跨域设置，origin设置为*，允许的方法设置GET,POST,DELETE,PUT,OPTIONS,不允许携带私密信息，允许的头部设置为*
     */
    public void applyDefaultConfiguration(){
        setAllowedOrigins(ALL);
        setAllowCredentials(false);
        setAllowedMethods(HttpMethod.OPTIONS,HttpMethod.GET,HttpMethod.POST,HttpMethod.DELETE,HttpMethod.PUT);
        setAllowedHeaders(ALL);
    }

    /**
     * 清理默认的跨域设置，orgin是空，允许的方法也不设置，允许的头也没有任何设置
     */
    public void clearDefaultConfiguration(){
        this.allowedOrigins.clear();
        setAllowCredentials(false);
        this.allowedMethods.clear();
        this.allowedHeaders.clear();
    }

    public String checkOrigin( String origin) {
        if (!StringUtils.hasText(origin)) {
            return null;
        }
        String originToCheck = trimTrailingSlash(origin);
        if (!ObjectUtils.isEmpty(this.allowedOrigins)) {
            if (this.allowedOrigins.contains(ALL)) {
                validateAllowCredentials();
                return ALL;
            }
            for (String allowedOrigin : this.allowedOrigins) {
                if (originToCheck.equalsIgnoreCase(allowedOrigin)) {
                    return origin;
                }
            }
        }
        return null;
    }

    private String trimTrailingSlash(String origin) {
        return (origin.endsWith("/") ? origin.substring(0, origin.length() - 1) : origin);
    }

    public void validateAllowCredentials() {
        if (this.allowCredentials == Boolean.TRUE &&
                this.allowedOrigins != null && this.allowedOrigins.contains(ALL)) {

            throw new IllegalArgumentException(
                    "When allowCredentials is true, allowedOrigins cannot contain the special value \"*\" " +
                            "since that cannot be set on the \"Access-Control-Allow-Origin\" response header. " +
                            "To allow credentials to a set of origins, list them explicitly " +
                            "or consider using \"allowedOriginPatterns\" instead.");
        }
    }

}
