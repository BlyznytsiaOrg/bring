package io.github.blyznytsiaorg.bring.core.context;

import io.github.blyznytsiaorg.bring.core.domain.BeanDefinition;

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
