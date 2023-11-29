package com.bobocode.bring.core.utils;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Qualifier;
import com.bobocode.bring.core.context.type.OrderComparator;
import com.bobocode.bring.core.exception.BeanPostProcessorConstructionLimitationException;
import com.bobocode.bring.core.exception.BringGeneralException;
import com.thoughtworks.paranamer.AnnotationParanamer;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public final class ReflectionUtils {
    private static final String BEAN_SHOULD_HAVE_MESSAGE = "BeanProcessor '%s' should have ";
    private static final String BEAN_SHOULD_HAVE_DEFAULT_CONSTRUCTOR_MESSAGE =
            BEAN_SHOULD_HAVE_MESSAGE + "only default constructor without params";
    private static final String BEAN_SHOULD_HAVE_CONSTRUCTOR_WITH_ONE_PARAMETER_MESSAGE =
            BEAN_SHOULD_HAVE_MESSAGE + "constructor with one parameter '%s'.";
    private static final String BEAN_SHOULD_HAVE_CONSTRUCTOR_WITH_PARAMETERS_MESSAGE =
            BEAN_SHOULD_HAVE_MESSAGE + "constructor with parameters '%s'.";
    public static final String DELIMITER = ", ";

    public static final OrderComparator ORDER_COMPARATOR = new OrderComparator();
    private static final Paranamer info = new CachingParanamer(new QualifierAnnotationParanamer(new BytecodeReadingParanamer()));
    private static final String ARG = "arg";

    private static final String SET_METHOD_START_PREFIX = "set";

    public static boolean isAutowiredSetterMethod(Method method) {
        return method.isAnnotationPresent(Autowired.class) && method.getName().startsWith(SET_METHOD_START_PREFIX);
    }

    public static Object getConstructorWithOutParameters(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception ex) {
            throw new BeanPostProcessorConstructionLimitationException(
                    String.format(BEAN_SHOULD_HAVE_DEFAULT_CONSTRUCTOR_MESSAGE, clazz.getSimpleName()));
        }
    }

    public static Object getConstructorWithOneParameter(Class<?> clazz, Class<?> parameterType, Object instance) {
        try {
            Constructor<?> constructor = clazz.getConstructor(parameterType);
            return constructor.newInstance(instance);
        } catch (Exception ex) {
            throw new BeanPostProcessorConstructionLimitationException(
                    String.format(BEAN_SHOULD_HAVE_CONSTRUCTOR_WITH_ONE_PARAMETER_MESSAGE,
                            clazz.getSimpleName(),
                            parameterType.getSimpleName()));
        }
    }

    public static Object getConstructorWithParameters(Class<?> clazz, Map<Class<?>, Object> parameterTypesToInstance) {
        try {
            Constructor<?> constructor = clazz.getConstructor(parameterTypesToInstance.keySet().toArray(new Class[0]));
            return constructor.newInstance(parameterTypesToInstance.values().toArray());
        } catch (Exception ex) {
            throw new BeanPostProcessorConstructionLimitationException(
                    String.format(BEAN_SHOULD_HAVE_CONSTRUCTOR_WITH_PARAMETERS_MESSAGE,
                            clazz.getSimpleName(),
                            parameterTypesToInstance.keySet().stream()
                                    .map(Class::getSimpleName)
                                    .collect(Collectors.joining(DELIMITER))));
        }
    }

    @SneakyThrows
    public static void setField(Field field, Object obj, Object value) {
        log.trace("Setting into field \"{}\" of {} the value {}", field.getName(), obj, value);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static List<String> getParameterNames(AccessibleObject methodOrConstructor) {
        return Arrays.stream(info.lookupParameterNames(methodOrConstructor)).toList();
    }

    public static int extractParameterPosition(Parameter parameter) {
        String name = parameter.getName();
        return Integer.parseInt(name.substring(name.indexOf(ARG) + ARG.length()));
    }

    @SneakyThrows
    public static List<Class<?>> extractImplClasses(ParameterizedType genericType, Reflections reflections,
                                                    List<Class<? extends Annotation>> createdBeanAnnotations) {
        Type actualTypeArgument = genericType.getActualTypeArguments()[0];
        if (actualTypeArgument instanceof Class actualTypeArgumentClass) {
            String name = actualTypeArgumentClass.getName();
            Class<?> interfaceClass = Class.forName(name);
            log.trace("Extracting implementations of {} for injection", interfaceClass.getName());

            return reflections.getSubTypesOf(interfaceClass)
                    .stream()
                    .filter(implementation -> isImplementationAnnotated(implementation, createdBeanAnnotations))
                    .sorted(ORDER_COMPARATOR)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private static boolean isImplementationAnnotated(Class<?> implementation, List<Class<? extends Annotation>> createdBeanAnnotations) {
        return Arrays.stream(implementation.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(createdBeanAnnotations::contains);
    }

    public static Supplier<Object> invokeBeanMethod(Method method, Object obj, Object[] params) {
        return () -> {
            try {
                return method.invoke(obj, params);
            } catch (Exception e) {
                throw new BringGeneralException(e);
            }
        };
    }

    public static Supplier<Object> createNewInstance(Constructor<?> constructor, Object[] args, Class<?> clazz,
                                                     boolean proxy) {
        return () -> {
            try {
                if (proxy) {
                    return ProxyUtils.createProxy(clazz, constructor, args);
                } else {
                    return constructor.newInstance(args);
                }
            } catch (Exception e) {
                throw new BringGeneralException(e);
            }
        };
    }

    private static class QualifierAnnotationParanamer extends AnnotationParanamer {

        public QualifierAnnotationParanamer(Paranamer fallback) {
            super(fallback);
        }

        @Override
        protected String getNamedValue(Annotation ann) {
            if (Objects.equals(Qualifier.class, ann.annotationType())) {
                Qualifier qualifier = (Qualifier) ann;
                return qualifier.value();
            } else {
                return null;
            }
        }

        @Override
        protected boolean isNamed(Annotation ann) {
            return Objects.equals(Qualifier.class, ann.annotationType());
        }
    }
}
