package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.context.type.ParameterValueTypeInjector;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.utils.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static com.bobocode.bring.core.utils.ReflectionUtils.isAutowiredSetterMethod;

/**
 * Responsible for injecting dependencies into setter methods of a bean using the @Autowired annotation.
 * Handles setter-based dependency injection for annotated setter methods within a class.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@AllArgsConstructor
@Slf4j
public class SetterBeanInjection {

    private final AnnotationBringBeanRegistry beanRegistry;
    private final ClassPathScannerFactory classPathScannerFactory;

    /**
     * Injects dependencies into setter methods of the provided class instance based on the @Autowired annotation.
     *
     * @param clazz The class whose setter methods are to be injected with dependencies.
     * @param bean  The instance of the class whose setter methods are being injected.
     */
    public void injectViaSetter(Class<?> clazz, Object bean) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(ReflectionUtils::isAutowiredSetterMethod)
                .forEach(method -> injectDependencyViaMethod(method, bean));
    }

    /**
     * Injects a dependency into a specific setter method of the provided bean instance.
     *
     * @param method The setter method to be injected with a dependency.
     * @param bean   The instance of the class whose setter method is being injected.
     */
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
                if (Objects.isNull(dependencyBeanName)) {
                    if (dependencyType.isInterface()) {
                        throw new NoSuchBeanException(String.format("No such bean that implements this %s ", dependencyType));
                    }
                    throw new NoSuchBeanException(dependencyType);
                }
                Object dependencyObject = beanRegistry.getOrCreateBean(dependencyBeanName, dependencyType, null);
                method.invoke(bean, dependencyObject);
            }
        }
    }

    /**
     * Injects a dependency into the setter method parameter of the provided bean instance.
     *
     * @param method   The setter method where the parameter is to be injected with a dependency.
     * @param parameter The parameter to be injected with a dependency.
     * @param bean     The instance of the class whose setter method is being injected.
     * @param valueType The ParameterValueTypeInjector responsible for setting the parameter value.
     * @return The injected dependency object.
     */
    @SneakyThrows
    private Object injectDependencyViaParameter(Method method, Parameter parameter, Object bean,
                                                ParameterValueTypeInjector valueType) {
        Object dependency = valueType.setValueToSetter(parameter, classPathScannerFactory.getCreatedBeanAnnotations());
        method.invoke(bean, dependency);
        return dependency;
    }
}
