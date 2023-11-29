package com.bobocode.bring.core.context;

import com.bobocode.bring.core.domain.BeanDefinition;

/**
 * Interface for registering bean definitions in a bean definition registry.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
public interface BeanDefinitionRegistry {

    /**
     * Registers a bean definition in the registry.
     *
     * @param beanDefinition The definition of the bean to be registered.
     */
    void registerBeanDefinition(BeanDefinition beanDefinition);
    
}
