package com.bobocode.bring.core.context;

import com.bobocode.bring.core.domain.BeanDefinition;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(BeanDefinition beanDefinition);
    
}
