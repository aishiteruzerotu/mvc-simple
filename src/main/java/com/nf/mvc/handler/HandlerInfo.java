package com.nf.mvc.handler;

public class HandlerInfo {
    Class<?> clz;

    public HandlerInfo(Class<?> clz) {
        this.clz = clz;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
