package com.bobocode.bring.core.bpp;

public interface BeanPostProcessor {

    default Object postProcessInitialization(Object bean, String beanName) {
        return bean;
    }
}
