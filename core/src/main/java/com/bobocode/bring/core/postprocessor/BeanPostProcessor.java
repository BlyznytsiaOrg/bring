package com.bobocode.bring.core.postprocessor;

public interface BeanPostProcessor {

    default Object postProcessInitialization(Object bean, String beanName) {
        return bean;
    }
    
}
