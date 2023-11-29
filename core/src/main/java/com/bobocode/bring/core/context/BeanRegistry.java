package com.bobocode.bring.core.context;

import com.bobocode.bring.core.domain.BeanDefinition;

/**
 * Interface for registering beans with their associated definitions in a bean registry.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
public interface BeanRegistry {

    /**
     * Registers a bean with its associated BeanDefinition in the registry.
     *
     * @param beanName       The name of the bean to be registered.
     * @param beanDefinition The definition of the bean being registered.
     */
    void registerBean(String beanName, BeanDefinition beanDefinition);
    
}
