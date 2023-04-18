package com.nf.mvc.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class ScanUtils {
    private ScanUtils(){}

    public static ScanResult scan(String scanPackage){
        ClassGraph classGraph = new ClassGraph();
        classGraph.enableAllInfo();
        classGraph.acceptClasses(scanPackage);
        return classGraph.scan();
    }
}
