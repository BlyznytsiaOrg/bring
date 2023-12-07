package io.github.blyznytsiaorg.bring.core.bfpp;

import io.github.blyznytsiaorg.bring.core.context.impl.DefaultBringBeanFactory;

/**
 * Interface to modify the bean factory before bean instantiation.
 * Usage:
 * To utilize this interface:
 * - Implement the postProcessBeanFactory method to define the logic to modify the bean factory.
 * Then, add annotation @BeanProcessor and Bring will automatically pick it up.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public interface BeanFactoryPostProcessor {

    /**
     * Modify the application context's internal bean factory.
     *
     * @param defaultBeanFactory The bean factory being initialized
     */
    void postProcessBeanFactory(DefaultBringBeanFactory defaultBeanFactory);
    
}
