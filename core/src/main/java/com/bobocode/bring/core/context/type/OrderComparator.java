package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.anotation.Order;

import java.util.Comparator;
import java.util.Objects;

/**
 * {@link Comparator} implementation for objects marked {@link Order}, sorting
 * by order value ascending,
 */
public class OrderComparator implements Comparator<Class<?>> {
    @Override
    public int compare(Class<?> o1, Class<?> o2) {
        return Comparator.comparingInt(this::getOrderValue)
                .compare(o1, o2);
    }

    private int getOrderValue(Class<?> clazz) {
        Order order = clazz.getAnnotation(Order.class);
        return Objects.nonNull(order) ? order.value() : Integer.MAX_VALUE;
    }
}
