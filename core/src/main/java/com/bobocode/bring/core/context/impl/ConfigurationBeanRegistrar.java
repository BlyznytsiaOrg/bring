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

@Slf4j
@AllArgsConstructor
public class ConfigurationBeanRegistrar {

    private final AnnotationBringBeanRegistry beanRegistry;

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
                                .orElseThrow(() -> new NoSuchBeanException(beanDefinition.getBeanClass()));
                        Object newObj = registerConfigurationBean(bd.getFactoryMethodName(), bd);
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
