package io.github.blyznytsiaorg.bring.core.context.impl;

import io.github.blyznytsiaorg.bring.core.context.scaner.ClassPathScannerFactory;
import io.github.blyznytsiaorg.bring.core.domain.BeanDefinition;
import io.github.blyznytsiaorg.bring.core.exception.NoSuchBeanException;
import io.github.blyznytsiaorg.bring.core.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Responsible for creating beans and injecting dependencies into these beans based on provided definitions.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Slf4j
public class BeanCreator {
    private final AnnotationBringBeanRegistry beanRegistry;
    private final ConstructorBeanInjection createBeanUsingConstructor;
    private final FieldBeanInjection fieldBeanInjection;
    private final SetterBeanInjection setterBeanInjection;

    /**
     * Constructs a BeanCreator.
     *
     * @param beanRegistry           The registry storing beans and their definitions.
     * @param classPathScannerFactory The factory for creating class path scanners.
     */
    public BeanCreator(AnnotationBringBeanRegistry beanRegistry, ClassPathScannerFactory classPathScannerFactory) {
        this.beanRegistry = beanRegistry;
        this.createBeanUsingConstructor = new ConstructorBeanInjection(beanRegistry, classPathScannerFactory);
        this.fieldBeanInjection = new FieldBeanInjection(beanRegistry, classPathScannerFactory);
        this.setterBeanInjection = new SetterBeanInjection(beanRegistry, classPathScannerFactory);
    }

    /**
     * Creates a bean of the specified class and injects dependencies into it.
     *
     * @param clazz          The class of the bean to be created.
     * @param beanName       The name of the bean being created.
     * @param beanDefinition The definition of the bean being created.
     * @return               The created bean object.
     */
    public Object create(Class<?> clazz, String beanName, BeanDefinition beanDefinition) {
        log.debug("Creating Bean \"{}\" of [{}]", beanName, clazz.getName());
        Object bean = createBeanUsingConstructor.create(clazz, beanName, beanDefinition);
        log.debug("Injecting dependencies to Bean \"{}\"", beanName);
        injectDependencies(clazz, bean);

        return bean;
    }

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

                        Object newObj = bd.isConfigurationBean()
                                ? registerConfigurationBean(beanName, bd)
                                : create(bd.getBeanClass(), beanName, bd);
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

    /**
     * Injects dependencies into the passed bean based on its class.
     *
     * @param clazz    The class of the bean.
     * @param bean     The bean.
     */
    private void injectDependencies(Class<?> clazz, Object bean) {
        fieldBeanInjection.injectViaFields(clazz, bean);
        setterBeanInjection.injectViaSetter(clazz, bean);
    }
}
