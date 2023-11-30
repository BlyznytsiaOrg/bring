package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.context.BeanDefinitionRegistry;
import com.bobocode.bring.core.context.BeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.exception.CyclicBeanException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.*;

/**
 * Registry class responsible for:
 * <ol>
 *      <li>Registering beans by bean name and bean definition: The bean name is created using 
 *      {@code AnnotationResolver} and is the key of the {@code DefaultBringBeanFactory.beanDefinitionMap}. 
 *      The bean definition contains all the necessary information that is needed to create a bean. Depending on the 
 *      bean scope, an object or a supplier will be stored in the application context.</li>
 *
 *      <li>Registering bean definitions: storing bean definitions in {@code DefaultBringBeanFactory.beanDefinitionMap}. 
 *      Those will be used in the future to create or retrieve beans.</li>
 * </ol>
 * 
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Slf4j
public class AnnotationBringBeanRegistry extends DefaultBringBeanFactory implements BeanRegistry, BeanDefinitionRegistry {

    @Getter
    protected ClassPathScannerFactory classPathScannerFactory;
    private final BeanCreator beanCreator;
    private final Set<String> currentlyCreatingBeans = new HashSet<>();
    @Getter
    private final Reflections reflections;

    public AnnotationBringBeanRegistry(Reflections reflections) {
        this.reflections = reflections;
        this.classPathScannerFactory = new ClassPathScannerFactory(reflections);
        this.beanCreator = new BeanCreator(this, classPathScannerFactory);
    }

    /**
     * Registers beans in the application context. Creates and stores singleton bean objects or suppliers for prototype beans.
     * Also defines the proper way to create beans depending on the type of the bean (annotated class or configuration bean) 
     * and injects dependant beans.
     * 
     * @param beanName       The name of the bean to be registered.
     * @param beanDefinition The definition of the bean being registered.
     */
    @Override
    public void registerBean(String beanName, BeanDefinition beanDefinition) {
        log.info("Registering Bean with name \"{}\" into Bring context...", beanName);

        Class<?> clazz = beanDefinition.getBeanClass();

        if (currentlyCreatingBeans.contains(beanName)) {
            log.error("Cyclic dependency detected!");
            throw new CyclicBeanException(currentlyCreatingBeans);
        }

        if (isBeanCreated(beanName)) {
            log.info("Bean with name \"{}\" already created, no need to register it again.", beanName);
            return;
        }

        currentlyCreatingBeans.add(beanName);

        if (beanDefinition.isConfigurationBean()) {
            beanCreator.registerConfigurationBean(beanName, beanDefinition);
        } else {
            beanCreator.create(clazz, beanName, beanDefinition);
        }

        currentlyCreatingBeans.clear();
    }

    /**
     * Stores a bean definition into {@code DefaultBringBeanFactory.beanDefinitionMap} by a name generated via 
     * {@code AnnotationResolver}. Beans are created based on these bean definitions. 
     * 
     * @param beanDefinition The definition of the bean to be registered.
     */
    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        String beanName = classPathScannerFactory.resolveBeanName(beanDefinition.getBeanClass());
        addBeanDefinition(beanName, beanDefinition);
    }

    public Object getOrCreateBean(String beanName) {
        Object existingBean = getBeanByName(beanName);

        if (Objects.nonNull(existingBean)) {
            return existingBean;
        }

        BeanDefinition beanDefinition = getBeanDefinitionMap().get(beanName);

        if (beanDefinition != null) {
            registerBean(beanName, beanDefinition);
        }

        return getBeanByName(beanName);
    }

}
