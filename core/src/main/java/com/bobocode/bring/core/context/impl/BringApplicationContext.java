package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.domain.BeanTypeEnum;
import com.bobocode.bring.core.postprocessor.BeanFactoryPostProcessor;
import com.bobocode.bring.core.postprocessor.BeanPostProcessor;
import com.bobocode.bring.core.postprocessor.BeanPostProcessorFactory;
import com.bobocode.bring.core.context.BringBeanFactory;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.postprocessor.impl.ConfigurationClassPostProcessor;
import org.reflections.Reflections;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class BringApplicationContext extends AnnotationBringBeanRegistry implements BringBeanFactory {

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = List.of(
            new ConfigurationClassPostProcessor());
    
    private final ClassPathScannerFactory classPathScannerFactory;
    
    private final BeanPostProcessorFactory beanPostProcessorFactory;
    
    private final Set<Class<?>> beansToCreate;

    public BringApplicationContext(String basePackage) {
        super(new Reflections(basePackage));
        this.classPathScannerFactory = new ClassPathScannerFactory(getReflections());
        this.beansToCreate = classPathScannerFactory.getBeansToCreate();
        this.beanPostProcessorFactory = new BeanPostProcessorFactory();

        // Create Bean definitions for classes annotated with annotations from ClassPathScanner
        register(beansToCreate);
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
        this.beanFactoryPostProcessors
                .forEach(processor -> processor.postProcessBeanFactory(this));
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
