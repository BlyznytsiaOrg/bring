package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.context.BeanDefinitionRegistry;
import com.bobocode.bring.core.context.BeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.exception.CyclicBeanException;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.exception.NoUniqueBeanException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.*;

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

    @Override
    public void registerBean(String beanName, BeanDefinition beanDefinition) {
        log.info("Registering Bean with name [{}] into Bring context...", beanName);

        Class<?> clazz = beanDefinition.getBeanClass();

        if (currentlyCreatingBeans.contains(beanName)) {
            throw new CyclicBeanException(currentlyCreatingBeans);
        }

        if (isBeanCreated(beanName)) {
            log.info("Bean with name [{}] already created, no need to register it again.", beanName);
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

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        String beanName = classPathScannerFactory.resolveBeanName(beanDefinition.getBeanClass());
        addBeanDefinition(beanName, beanDefinition);
    }

    public Object getOrCreateBean(String beanName, Class<?> beanType, String qualifier) {
        Object existingBean = getBeanByName(beanName);

        if (Objects.nonNull(existingBean)) {
            return existingBean;
        }

        BeanDefinition beanDefinition = getBeanDefinitionMap().get(beanName);

        if (Objects.isNull(beanDefinition)) {
            //TODO this is like workaround need to think how to populate bean definition for interface & dependencies
            if (beanType.isInterface()) {
                registerImplementations(beanName, beanType, qualifier);
            }
        } else {
            registerBean(beanName, beanDefinition);
        }

        return getBeanByName(beanName);
    }

    private <T> void registerImplementations(String beanName, Class<T> interfaceType, String qualifier) {
        Set<Class<? extends T>> implementations = reflections.getSubTypesOf(interfaceType);

        List<BeanDefinition> beanDefinitionsForRegistration = new ArrayList<>();

        for (Class<? extends T> implementation : implementations) {
            boolean hasRequiredAnnotation = Arrays.stream(implementation.getAnnotations())
                    .map(Annotation::annotationType)
                    .anyMatch(classPathScannerFactory.getCreatedBeanAnnotations()::contains);

            if (hasRequiredAnnotation) {
                List<String> beanNames = Optional.ofNullable(getTypeToBeanNames().get(implementation))
                        .orElseThrow(() -> {
                            if (implementation.isInterface()) {
                                return new NoSuchBeanException(String.format("No such bean that implements this %s ", implementation));
                            }
                            return new NoSuchBeanException(implementation);
                        });

                if (beanNames.size() != 1) {
                    throw new NoUniqueBeanException(implementation);
                }

                String implementationBeanName = beanNames.get(0);
                BeanDefinition beanDefinition = getBeanDefinitionMap().get(implementationBeanName);
                beanDefinitionsForRegistration.add(beanDefinition);
            }
        }
        findPrimaryOrQualifierAndRegisterBean(beanName, beanDefinitionsForRegistration, interfaceType, qualifier);
    }

    private <T> void findPrimaryOrQualifierAndRegisterBean (String beanName, List<BeanDefinition> beanDefinitions,
                                                            Class<T> interfaceType, String qualifier) {
        if (beanDefinitions.size() > 1){
            var beanDefinitionsForRegistration = beanDefinitions.stream()
                    .filter(BeanDefinition::isPrimary
                    ).toList();
            if(beanDefinitionsForRegistration.size() > 1) {
                throw new NoUniqueBeanException(interfaceType);
            } else if (beanDefinitionsForRegistration.isEmpty()) {
                //check qualifier
                beanDefinitionsForRegistration = beanDefinitions.stream()
                        .filter(bd -> bd.getFactoryBeanName().equals(qualifier))
                        .toList();
                if(beanDefinitionsForRegistration.size() != 1){
                    throw new NoUniqueBeanException(interfaceType);
                }
            }
            registerBean(beanName, beanDefinitionsForRegistration.get(0));
        } else if (beanDefinitions.size() == 1) {
            registerBean(beanName, beanDefinitions.get(0));
        }
    }

}
