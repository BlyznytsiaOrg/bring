package com.bobocode.bring.core.postprocessor.impl;

import com.bobocode.bring.core.anotation.Bean;
import com.bobocode.bring.core.context.impl.DefaultBringBeanFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.utils.BeanScopeUtils;
import com.bobocode.bring.core.domain.BeanTypeEnum;
import com.bobocode.bring.core.postprocessor.BeanFactoryPostProcessor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ConfigurationClassPostProcessor implements BeanFactoryPostProcessor {
    
    @Override
    public void postProcessBeanFactory(DefaultBringBeanFactory defaultBeanFactory) {
        defaultBeanFactory.getAllBeanDefinitionNames()
                .forEach(beanName -> {
                    BeanDefinition beanDefinition = defaultBeanFactory.getBeanDefinitionByName(beanName);
                    
                    if (beanDefinition.isConfiguration()) {
                      List<Method> declaredAnnotatedMethods = this.getDeclaredAnnotatedMethods(beanDefinition);
                      declaredAnnotatedMethods.forEach(method -> 
                        this.loadBeanDefinitionsForBeanMethod(defaultBeanFactory, beanName, method));
                    }
                });
    }
    
    private List<Method> getDeclaredAnnotatedMethods(BeanDefinition beanDefinition) {
      return Arrays.stream(beanDefinition.getBeanClass().getDeclaredMethods())
        .filter(method -> method.isAnnotationPresent(Bean.class))
        .toList();
    }

    private void loadBeanDefinitionsForBeanMethod(DefaultBringBeanFactory defaultBeanFactory, String beanName, 
                                                  Method method) {
        BeanDefinition beanDefinition = BeanDefinition.builder()
            .beanClass(method.getReturnType())
            .beanType(BeanTypeEnum.findBeanType(method))
            .scope(BeanScopeUtils.findBeanScope(method))
            .method(method)
            .factoryMethodName(method.getName())
            .factoryBeanName(beanName)
            .build();
        
          defaultBeanFactory.addBeanDefinition(beanDefinition.getFactoryMethodName(), beanDefinition);
      }

}
