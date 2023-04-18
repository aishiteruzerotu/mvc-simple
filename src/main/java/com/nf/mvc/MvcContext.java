package com.nf.mvc;

import io.github.classgraph.ScanResult;

public class MvcContext {

    private static final MvcContext instance = new MvcContext();

    private ScanResult scanResult;
    private MvcContext(){}


    public static MvcContext getMvcContext(){
        return instance;
    }

    public void config(ScanResult scanResult) {
        this.scanResult =scanResult;
    }

    public ScanResult getScanResult(){
        return  this.scanResult;
    }
}
