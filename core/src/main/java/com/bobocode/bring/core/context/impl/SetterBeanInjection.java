package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Value;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.context.type.ParameterValueTypeInjector;
import com.bobocode.bring.core.utils.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.bobocode.bring.core.utils.ReflectionUtils.isAutowiredSetterMethod;

@AllArgsConstructor
@Slf4j
public class SetterBeanInjection {

    private final AnnotationBringBeanRegistry beanRegistry;
    private final ClassPathScannerFactory classPathScannerFactory;

    public void injectViaSetter(Class<?> clazz, Object bean) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(ReflectionUtils::isAutowiredSetterMethod)
                .forEach(method -> injectDependencyViaMethod(method, bean));
    }

    @SneakyThrows
    private void injectDependencyViaMethod(Method method, Object bean) {
        if (isAutowiredSetterMethod(method)) {
            Parameter parameter = method.getParameters()[0];
            Optional<Object> injectViaProperties = beanRegistry.getTypeResolverFactory()
                    .getParameterValueTypeInjectors().stream()
                    .filter(valueType -> valueType.hasAnnotatedWithValue(parameter))
                    .map(valueType -> injectDependencyViaParameter(method, parameter, bean, valueType))
                    .findFirst();

            if (injectViaProperties.isEmpty()) {
                Class<?> dependencyType = method.getParameterTypes()[0];
                String dependencyBeanName = classPathScannerFactory.resolveBeanName(dependencyType);
                Object dependencyObject = beanRegistry.getOrCreateBean(dependencyBeanName, dependencyType, null);
                method.invoke(bean, dependencyObject);
            }
        }
    }

    @SneakyThrows
    private Object injectDependencyViaParameter(Method method, Parameter parameter, Object bean,
                                                ParameterValueTypeInjector valueType) {
        Object dependencyValue = valueType.setValueToSetter(parameter, classPathScannerFactory.getCreatedBeanAnnotations());
        if (dependencyValue instanceof List) {
            var dependencyObjects = beanRegistry.injectListDependency((List<Class<?>>) dependencyValue);
            method.invoke(bean, dependencyObjects);
        } else {
            method.invoke(bean, dependencyValue);
        }
        return dependencyValue;
    }
}
