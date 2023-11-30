package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.annotation.Order;

import java.util.Comparator;
import java.util.Objects;

/**
 * {@link Comparator} implementation for objects marked {@link Order}, sorting
 * by order value ascending,
 */
public class OrderComparator implements Comparator<Class<?>> {

    /**
     * Compares two Class objects based on their associated Order annotation values.
     *
     * @param o1 The first Class object.
     * @param o2 The second Class object.
     * @return A negative integer, zero, or a positive integer as the first class has a lower, equal, or higher Order value than the second class.
     */
    @Override
    public int compare(Class<?> o1, Class<?> o2) {
        return Comparator.comparingInt(this::getOrderValue)
                .compare(o1, o2);
    }

    /**
     * Retrieves the Order annotation value associated with a Class.
     *
     * @param clazz The Class object for which the Order annotation value is retrieved.
     * @return The Order annotation value if present; otherwise, returns Integer.MAX_VALUE.
     */
    private int getOrderValue(Class<?> clazz) {
        Order order = clazz.getAnnotation(Order.class);
        return Objects.nonNull(order) ? order.value() : Integer.MAX_VALUE;
    }
}
