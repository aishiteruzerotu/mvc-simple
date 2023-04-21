package com.nf.mvc.support;

import java.util.Comparator;

public class OrderComparator<T> implements Comparator<T> {
    public OrderComparator() {
    }

    @Override
    public int compare(T o1, T o2) {
        this.isNotOrder(o1);
        this.isNotOrder(o2);
        return this.getOrder(o1) - this.getOrder(o2);
    }

    private Class<?> getClass(T t) {
        return t.getClass();
    }

    private boolean isOrder(T t) {
        return this.getClass(t).isAnnotationPresent(Order.class);
    }

    private void isNotOrder(T t) {
        if (!this.isOrder(t)) {
            String clzName = this.getClass(t).getName();
            throw new IllegalArgumentException("无法获取 " + clzName + " 的 "
                    + Order.class + " 注解" + "\n"
                    + "\t使用者提供的扩展库需要有 " +Order.class+" 注解，该注解用于比较排序使用者提供的扩展，注解的数值越小，优先级越高"
            );
        }
    }

    private int getOrder(T t) {
        return this.getClass(t).getAnnotation(Order.class).value();
    }
}
