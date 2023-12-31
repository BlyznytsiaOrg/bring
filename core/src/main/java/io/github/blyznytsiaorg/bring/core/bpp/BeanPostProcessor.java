package io.github.blyznytsiaorg.bring.core.bpp;


/**
 * Interface for post-processing beans during their initialization phase.
 * Implementations of this interface can intercept bean creation and manipulate
 * the bean instance initialization.
 * Then, add annotation @BeanProcessor and Bring will automatically pick it up.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
public interface BeanPostProcessor {

    /**
     * Process the given bean instance after its initialization.
     *
     * @param bean     the bean instance
     * @param beanName the name of the bean
     * @return the processed bean instance
     */
    default Object postProcessInitialization(Object bean, String beanName) {
        return bean;
    }
    
}
