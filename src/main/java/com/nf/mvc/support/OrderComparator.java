package com.nf.mvc.support;

import java.lang.reflect.AnnotatedElement;
import java.util.Comparator;

/**
 * 使用者提供的扩展库需要有 @Order 注解，该注解用于比较排序使用者提供的扩展，注解的数值越小，优先级越高
 * @param <T>
 */
public class OrderComparator<T> implements Comparator<T> {
    public OrderComparator() {
    }

    @Override
    public int compare(T o1, T o2) {
        return this.getOrder(o1) - this.getOrder(o2);
    }

    private Class<?> getClass(T t) {
        return t.getClass();
    }

    private int getOrder(T t) {
        return this.getOrderValuer(this.getClass(t));
    }

    private int getOrderValuer(AnnotatedElement element) {
        int result = Integer.MAX_VALUE;
        if (element.isAnnotationPresent(Order.class)) {
            Order controller = element.getDeclaredAnnotation(Order.class);
            result = controller.value();
        }
        return result;
    }
}
