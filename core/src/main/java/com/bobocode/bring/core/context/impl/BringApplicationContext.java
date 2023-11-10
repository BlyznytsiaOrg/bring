package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.bpp.BeanPostProcessor;
import com.bobocode.bring.core.context.BringBeanFactory;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class BringApplicationContext extends AnnotationBringBeanRegistry implements BringBeanFactory {

    public BringApplicationContext(String basePackage) {
        super(new Reflections(basePackage));
        Set<Class<?>> classesThatAnnotatedWithComponentOrService = new HashSet<>();
        getCreatedBeanAnnotations().forEach(annotation -> classesThatAnnotatedWithComponentOrService.addAll(getReflections().getTypesAnnotatedWith(annotation)));
        classesThatAnnotatedWithComponentOrService.forEach(this::registerBean);
        beanPostProcessors();
    }

    public <T> BringApplicationContext(Class<T> componentClass) {
       this(componentClass.getPackageName());
    }

    private void beanPostProcessors() {
        Set<Class<? extends BeanPostProcessor>> beanPostProcessors = getReflections().getSubTypesOf(BeanPostProcessor.class);
        getAllBeans().forEach((beanName, bean) -> {
            for (var beanPostProcessor : beanPostProcessors) {
                var bpp = getBean(beanPostProcessor);
                Object beanAfterPostProcess = bpp.postProcessBeforeInitialization(bean, beanName);
                getBeansNameToObject().put(beanName, beanAfterPostProcess);
            }
        });
    }

}
