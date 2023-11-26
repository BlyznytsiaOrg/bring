package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.utils.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Responsible for registering configuration beans within the DefaultBringBeanFactory context.
 * This class facilitates the instantiation and management of beans defined within configuration classes.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
@AllArgsConstructor
public class ConfigurationBeanRegistry {

    private final DefaultBringBeanFactory beanRegistry;

    /**
     * Registers a configuration bean in the context based on the provided bean name and definition.
     * Resolves dependencies and instantiates the configuration bean within the context.
     *
     * @param beanName       The name of the configuration bean to be registered.
     * @param beanDefinition The definition of the configuration bean.
     * @return The registered configuration bean.
     */
    public Object registerConfigurationBean(String beanName, BeanDefinition beanDefinition) {
        Object configObj = Optional.ofNullable(beanRegistry.getSingletonObjects().get(beanDefinition.getFactoryBeanName()))
                .orElseThrow(() -> {
                    log.info("Unable to register Bean from Configuration class [{}]: " +
                            "Configuration class not annotated or is not of Singleton scope.", beanName);
                    return new NoSuchBeanException(beanDefinition.getBeanClass());
                });

        List<String> methodParamNames = ReflectionUtils.getParameterNames(beanDefinition.getMethod());

        List<Object> methodObjs = new ArrayList<>();
        methodParamNames.forEach(
                paramName -> {
                    Object object = beanRegistry.getBeanByName(paramName);

                    if (Objects.nonNull(object)) {
                        methodObjs.add(object);
                    } else {
                        BeanDefinition bd = Optional.ofNullable(beanRegistry.getBeanDefinitionMap().get(paramName))
                                .orElseThrow(() -> {
                                    if (beanDefinition.getBeanClass().isInterface()) {
                                        return new NoSuchBeanException(String.format("No such bean that implements this %s",
                                                beanDefinition.getBeanClass()));
                                    }
                                    return new NoSuchBeanException(beanDefinition.getBeanClass());
                                });
                        Object newObj = registerConfigurationBean(beanName, bd);
                        methodObjs.add(newObj);
                    }
                });

        Supplier<Object> supplier = ReflectionUtils.invokeBeanMethod(beanDefinition.getMethod(),
                configObj, methodObjs.toArray());
        Object bean = supplier.get();

        if (beanDefinition.isPrototype()) {
            beanRegistry.addPrototypeBean(beanName, supplier);
        } else {
            beanRegistry.addSingletonBean(beanName, bean);
        }

        return bean;
    }
}
