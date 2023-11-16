package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.context.BringBeanFactory;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.context.type.TypeResolverFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.domain.BeanTypeEnum;
import com.bobocode.bring.core.postprocessor.BeanPostProcessor;
import com.bobocode.bring.core.postprocessor.BeanPostProcessorDefinitionFactory;
import com.bobocode.bring.core.postprocessor.BeanPostProcessorFactory;
import org.reflections.Reflections;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
    
    private final ClassPathScannerFactory classPathScannerFactory;
    
    private final BeanPostProcessorFactory beanPostProcessorFactory;

    private final BeanPostProcessorDefinitionFactory beanPostProcessorDefinitionFactory;
    
    private final Set<Class<?>> beansToCreate;

    /**
     * Constructs a new BringApplicationContext with the specified base package for component scanning.
     *
     * @param basePackage the base package to scan for annotated beans
     * @see Reflections
     * @see ClassPathScannerFactory
     * @see BeanPostProcessorDefinitionFactory
     * @see BeanPostProcessorFactory
     */
    public BringApplicationContext(String basePackage) {
        super(new Reflections(basePackage));
        this.classPathScannerFactory = new ClassPathScannerFactory(getReflections());
        this.beansToCreate = classPathScannerFactory.getBeansToCreate();
        this.beanPostProcessorDefinitionFactory =  new BeanPostProcessorDefinitionFactory();
        // Create Bean definitions for classes annotated with annotations from ClassPathScanner
        register(beansToCreate);
        this.beanPostProcessorFactory = new BeanPostProcessorFactory();
    }

    public <T> BringApplicationContext(Class<T> componentClass) {
       this(componentClass.getPackageName());
    }

    private void register(Set<Class<?>> classes) {
        classes.forEach(clazz -> {
            BeanDefinition beanDefinition = BeanDefinition.builder()
                    .beanClass(clazz)
                    .beanType(BeanTypeEnum.findBeanType(clazz))
                    .isSingleton(true)
                    .factoryBeanName(clazz.getSimpleName())
                    .build();

            registerBeanDefinition(beanDefinition);
        });
    }
    
    public void refresh() {
        // Create additional Bean definitions i.e. for Beans in Configuration classes
        invokeBeanFactoryPostProcessors();
        
        invokeBeanPostProcessors();
        
        // Create Singleton Bean objects from Bean definitions
        instantiateBeans();
    }

    private void invokeBeanFactoryPostProcessors() {
        beanPostProcessorDefinitionFactory.getBeanFactoryPostProcessors()
                .forEach(processor -> processor.postProcessBeanFactory(this));

        setTypeResolverFactory(new TypeResolverFactory(getProperties(), getReflections()));
    }

    private void invokeBeanPostProcessors() {
        List<BeanPostProcessor> beanPostProcessors = beanPostProcessorFactory.getBeanPostProcessors();
        getAllBeans().forEach((beanName, bean) -> {
            for (var beanPostProcessor : beanPostProcessors) {
                Object beanAfterPostProcess = beanPostProcessor.postProcessInitialization(bean, beanName);
                getSingletonObjects().put(beanName, beanAfterPostProcess);
            }
        });
    }

    private void instantiateBeans() {
        getBeanDefinitions().entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().getBeanType().getOrder()))
                .forEach(entry -> registerBean(entry.getKey(), entry.getValue()));
    }

}
