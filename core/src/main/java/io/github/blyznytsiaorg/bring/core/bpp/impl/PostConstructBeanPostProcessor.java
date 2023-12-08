package io.github.blyznytsiaorg.bring.core.bpp.impl;

import io.github.blyznytsiaorg.bring.core.annotation.BeanProcessor;
import io.github.blyznytsiaorg.bring.core.annotation.PostConstruct;
import io.github.blyznytsiaorg.bring.core.bpp.BeanPostProcessor;
import io.github.blyznytsiaorg.bring.core.exception.PostConstructException;
import io.github.blyznytsiaorg.bring.core.utils.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * The {@code PostConstructBeanPostProcessor} is a custom implementation of the
 * {@code BeanPostProcessor} interface in the Bring framework, designed to
 * handle post-construction tasks for beans.
 *
 * <p>This processor specifically targets methods annotated with the {@code @PostConstruct}
 * annotation, providing a mechanism to execute custom logic after a bean has been
 * instantiated.
 *
 * @see BeanPostProcessor
 * @see PostConstruct
 */
@BeanProcessor
public class PostConstructBeanPostProcessor implements BeanPostProcessor {

    /**
     * Execute methods annotated with @PostConstruct.
     *
     * @param bean     the bean instance
     * @param beanName the name of the bean
     * @return the processed bean
     */
    @Override
    public Object postProcessInitialization(Object bean, String beanName) {
        Method[] declaredMethods = bean.getClass().getMethods();
        try {
            ReflectionUtils.processBeanPostProcessorAnnotation(bean, declaredMethods, PostConstruct.class);
        } catch (Exception exception) {
            throw new PostConstructException(exception);
        }

        return BeanPostProcessor.super.postProcessInitialization(bean, beanName);
    }
}
