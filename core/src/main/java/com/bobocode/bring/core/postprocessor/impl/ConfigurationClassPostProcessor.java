package com.bobocode.bring.core.postprocessor.impl;

import com.bobocode.bring.core.context.impl.DefaultBringBeanFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.domain.BeanTypeEnum;
import com.bobocode.bring.core.postprocessor.BeanFactoryPostProcessor;

import java.lang.reflect.Method;

public class ConfigurationClassPostProcessor implements BeanFactoryPostProcessor {
    
    @Override
    public void postProcessBeanFactory(DefaultBringBeanFactory defaultBeanFactory) {
        defaultBeanFactory.getAllBeanDefinitionNames()
                .forEach(beanName -> {
                    BeanDefinition beanDefinition = defaultBeanFactory.getBeanDefinitionByName(beanName);
                    
                    if (beanDefinition.getBeanType() == BeanTypeEnum.CONFIGURATION) {
                      for (Method method : beanDefinition.getBeanClass().getDeclaredMethods()) {
                        this.loadBeanDefinitionsForBeanMethod(defaultBeanFactory, beanName, method);
                      }
                    }
                });
    }

    private void loadBeanDefinitionsForBeanMethod(DefaultBringBeanFactory defaultBeanFactory, String beanName, 
                                                  Method method) {
        BeanDefinition beanDefinition = BeanDefinition.builder()
            .beanClass(method.getReturnType())
            .beanType(BeanTypeEnum.findBeanType(method))
            .isSingleton(true)
            .method(method)
            .factoryMethodName(method.getName())
            .factoryBeanName(beanName)
            .build();
        
          defaultBeanFactory.addBeanDefinition(beanDefinition.getFactoryMethodName(), beanDefinition);
      }

}
