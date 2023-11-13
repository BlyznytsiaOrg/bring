package com.bobocode.bring.core.context;

import com.bobocode.bring.core.domain.BeanDefinition;

public interface BeanRegistry {

    void registerBean(String beanName, BeanDefinition beanDefinition);
    
}
