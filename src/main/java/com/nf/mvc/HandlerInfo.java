package com.nf.mvc;

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
