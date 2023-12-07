package io.github.blyznytsiaorg.bring.core.bfpp.impl;

import io.github.blyznytsiaorg.bring.core.annotation.Bean;
import io.github.blyznytsiaorg.bring.core.annotation.BeanProcessor;
import io.github.blyznytsiaorg.bring.core.annotation.Primary;
import io.github.blyznytsiaorg.bring.core.context.impl.DefaultBringBeanFactory;
import io.github.blyznytsiaorg.bring.core.domain.BeanDefinition;
import io.github.blyznytsiaorg.bring.core.domain.BeanTypeEnum;
import io.github.blyznytsiaorg.bring.core.bfpp.BeanFactoryPostProcessor;
import io.github.blyznytsiaorg.bring.core.exception.NoUniqueBeanException;
import io.github.blyznytsiaorg.bring.core.utils.BeanScopeUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * This post processor registers bean definitions in the DefaultBringBeanFactory for beans - 
 * methods annotated with {@code Bean} annotation. These Beans should be in a Configuration class - a class
 * annotated with {@code @Configuration} annotation.
 * The Bean name for a bean defined in a Configuration class is the value taken from the {@code Bean}
 * annotation of the method name.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@BeanProcessor
public class ConfigurationClassPostProcessor implements BeanFactoryPostProcessor {

    /**
     * Processes the DefaultBringBeanFactory to populate the bean definitions map with beans defined in
     * Configuration classes.
     *
     * @param defaultBeanFactory The DefaultBringBeanFactory to be processed.
     */
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
        
        String configBeanName = resolveBeanName(method);
        if (defaultBeanFactory.getBeanDefinitionMap().containsKey(configBeanName)) {
            throw new NoUniqueBeanException(configBeanName);
        }
        
        defaultBeanFactory.addBeanDefinition(configBeanName, beanDefinition);
      }
      
      private String resolveBeanName(Method method) {
          String value = method.getAnnotation(Bean.class).value();
          return value.isEmpty() ? method.getName() : value;
      }

}
