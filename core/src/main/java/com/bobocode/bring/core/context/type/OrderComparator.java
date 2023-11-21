package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.anotation.Order;

import java.util.Comparator;
import java.util.Objects;

/**
 * {@link Comparator} implementation for objects marked {@link Order} , sorting
 * by order value ascending,
 */
public class OrderComparator implements Comparator<Class<?>> {
    @Override
    public int compare(Class<?> o1, Class<?> o2) {
        if (!Objects.isNull(o1.getAnnotation(Order.class))) {
            if (Objects.isNull(o2.getAnnotation(Order.class))) {
                return -1;
            } else {
                return Integer.compare(o1.getAnnotation(Order.class).value(), o2.getAnnotation(Order.class).value());
            }
        } else {
            if (!Objects.isNull(o2.getAnnotation(Order.class))) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
