package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.bpp.BeanPostProcessor;
import com.bobocode.bring.core.bpp.BeanPostProcessorFactory;
import com.bobocode.bring.core.context.BringBeanFactory;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;

public class BringApplicationContext extends AnnotationBringBeanRegistry implements BringBeanFactory {

    private final ClassPathScannerFactory classPathScannerFactory;
    private final BeanPostProcessorFactory beanPostProcessorFactory;
    private final Set<Class<?>> beansToCreate;

    public BringApplicationContext(String basePackage) {
        super(new Reflections(basePackage));
        this.classPathScannerFactory = new ClassPathScannerFactory(getReflections());
        this.beansToCreate = classPathScannerFactory.getBeansToCreate();
        this.beanPostProcessorFactory = new BeanPostProcessorFactory();

        //TODO bean definition and creation beans
        this.beansToCreate.forEach(this::registerBean);

        beanPostProcessors();
    }

    public <T> BringApplicationContext(Class<T> componentClass) {
       this(componentClass.getPackageName());
    }

    private void beanPostProcessors() {
        List<BeanPostProcessor> beanPostProcessors = beanPostProcessorFactory.getBeanPostProcessors();
        getAllBeans().forEach((beanName, bean) -> {
            for (var beanPostProcessor : beanPostProcessors) {
                Object beanAfterPostProcess = beanPostProcessor.postProcessInitialization(bean, beanName);
                getBeansNameToObject().put(beanName, beanAfterPostProcess);
            }
        });
    }

}
