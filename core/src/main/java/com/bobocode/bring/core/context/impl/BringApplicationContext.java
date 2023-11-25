package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.anotation.Primary;
import com.bobocode.bring.core.context.BringBeanFactory;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.context.type.TypeResolverFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.utils.BeanScopeUtils;
import com.bobocode.bring.core.domain.BeanTypeEnum;
import com.bobocode.bring.core.bpp.BeanPostProcessor;
import com.bobocode.bring.core.postprocessor.BeanPostProcessorDefinitionFactory;
import com.bobocode.bring.core.bpp.BeanPostProcessorFactory;
import org.reflections.Reflections;

import java.util.*;

/**
 * The {@code BringApplicationContext} class represents the core context for managing and initializing
 * the Bring application's beans. It extends {@link AnnotationBringBeanRegistry} to provide bean registration
 * capabilities based on annotations and implements {@link BringBeanFactory} to define bean creation methods.
 *
 * <p>The class is initialized with a base package for component scanning, and it performs the following steps:
 * <ol>
 *     <li>Uses {@link Reflections} to scan the specified base package for classes with relevant annotations.</li>
 *     <li>Creates a {@link ClassPathScannerFactory} to obtain a set of classes marked for bean creation.</li>
 *     <li>Creates a {@link BeanPostProcessorDefinitionFactory} to handle bean post-processor definitions.</li>
 *     <li>Registers Bean definitions for classes annotated with annotations from the classpath scan.</li>
 *     <li>Creates a {@link BeanPostProcessorFactory} to handle bean post-processors.</li>
 * </ol>
 *
 * <p>The class maintains the following important fields:
 * <ul>
 *     <li>{@code classPathScannerFactory}: Factory for creating a class path scanner for component scanning.</li>
 *     <li>{@code beanPostProcessorFactory}: Factory for creating bean post-processors.</li>
 *     <li>{@code beanPostProcessorDefinitionFactory}: Factory for handling bean post-processor definitions.</li>
 *     <li>{@code beansToCreate}: Set of classes annotated for bean creation obtained from the class path scanner.</li>
 * </ul>
 *
 * <p>Usage example:
 * <pre>{@code
 * BringApplicationContext context = new BringApplicationContext("com.example.myapp");
 * }</pre>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 * @see AnnotationBringBeanRegistry
 * @see BringBeanFactory
 * @see ClassPathScannerFactory
 * @see BeanPostProcessorFactory
 * @see BeanPostProcessorDefinitionFactory
 */
public class BringApplicationContext extends AnnotationBringBeanRegistry implements BringBeanFactory {

    private final BeanPostProcessorDefinitionFactory beanPostProcessorDefinitionFactory;

    /**
     * Constructs a new BringApplicationContext with the specified base package for component scanning.
     *
     * @param basePackage the base package to scan for annotated beans
     * @see Reflections
     * @see ClassPathScannerFactory
     * @see BeanPostProcessorDefinitionFactory
     * @see BeanPostProcessorFactory
     */
    public BringApplicationContext(String... basePackage) {
        super(new Reflections(basePackage));
        this.beanPostProcessorDefinitionFactory =  new BeanPostProcessorDefinitionFactory();
        // Create Bean definitions for classes annotated with annotations from ClassPathScanner
        register(classPathScannerFactory.getBeansToCreate());
    }

    public <T> BringApplicationContext(Class<T> componentClass) {
       this(componentClass.getPackageName());
    }

    private void register(Set<Class<?>> classes) {
        classes.forEach(clazz -> {
            BeanDefinition beanDefinition = BeanDefinition.builder()
                    .beanClass(clazz)
                    .beanType(BeanTypeEnum.findBeanType(clazz))
                    .scope(BeanScopeUtils.findBeanScope(clazz))
                    .proxyMode(BeanScopeUtils.findProxyMode(clazz))
                    .factoryBeanName(clazz.getSimpleName())
                    .isPrimary(clazz.isAnnotationPresent(Primary.class))
                    .build();

            registerBeanDefinition(beanDefinition);
        });
    }
    
    public void refresh() {
        // Create additional Bean definitions i.e. for Beans in Configuration classes
        invokeBeanFactoryPostProcessors();

        // Create Singleton Bean objects from Bean definitions
        instantiateBeans();

        invokeBeanPostProcessors();
    }

    private void invokeBeanFactoryPostProcessors() {
        beanPostProcessorDefinitionFactory.getBeanFactoryPostProcessors()
                .forEach(processor -> processor.postProcessBeanFactory(this));

        setTypeResolverFactory(new TypeResolverFactory(getProperties(), getReflections(), this));
    }

    private void invokeBeanPostProcessors() {
        List<BeanPostProcessor> beanPostProcessors = new BeanPostProcessorFactory(this).getBeanPostProcessors();
        getAllBeans().forEach((beanName, bean) -> {
            for (var beanPostProcessor : beanPostProcessors) {
                Object beanAfterPostProcess = beanPostProcessor.postProcessInitialization(bean, beanName);
                getSingletonObjects().put(beanName, beanAfterPostProcess);
            }
        });
    }

    private void instantiateBeans() {
        getBeanDefinitionMap().entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().getBeanType().getOrder()))
                .forEach(entry -> registerBean(entry.getKey(), entry.getValue()));
    }

}
