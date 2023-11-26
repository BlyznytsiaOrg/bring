package com.bobocode.bring.core.postprocessor.impl;

import com.bobocode.bring.core.anotation.Bean;
import com.bobocode.bring.core.anotation.BeanProcessor;
import com.bobocode.bring.core.anotation.Primary;
import com.bobocode.bring.core.context.impl.DefaultBringBeanFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.domain.BeanTypeEnum;
import com.bobocode.bring.core.postprocessor.BeanFactoryPostProcessor;
import com.bobocode.bring.core.utils.BeanScopeUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@BeanProcessor
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
            .proxyMode(BeanScopeUtils.findProxyMode(method))
            .method(method)
            .factoryBeanName(beanName)
            .isPrimary(method.isAnnotationPresent(Primary.class))
            .build();
        
          defaultBeanFactory.addBeanDefinition(resolveBeanName(method), beanDefinition);
      }
      
      private String resolveBeanName(Method method) {
          String value = method.getAnnotation(Bean.class).value();
          return value.isEmpty() ? method.getName() : value;
      }

}
