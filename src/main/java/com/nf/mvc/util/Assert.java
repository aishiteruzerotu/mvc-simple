package com.nf.mvc.util;

/**
 * 拷贝自spring，原本是个抽象类，只保留了一个方法
 * 这里特意改成接口，演示工具类在新jdk用接口实现是很好的
 */
public interface Assert {
     static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
