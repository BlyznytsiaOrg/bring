package com.bobocode.bring.core.context;

import com.bobocode.bring.core.domain.BeanDefinition;

import java.util.List;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanName);

    List<String> getBeanDefinitionNames();
    
}
