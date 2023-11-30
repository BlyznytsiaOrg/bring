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


/**
 * Utility class providing reflection-related functionalities, including methods for constructor instantiation,
 * field manipulation, method invocation, and annotation processing.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
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
    private static final String DELIMITER = ", ";

    public static final OrderComparator ORDER_COMPARATOR = new OrderComparator();
    private static final Paranamer info = new CachingParanamer(new QualifierAnnotationParanamer(new BytecodeReadingParanamer()));
    private static final String ARG = "arg";

    private static final String SET_METHOD_START_PREFIX = "set";

    /**
     * Checks if the given method is an autowired setter method.
     *
     * @param method The method to be checked
     * @return True if the method is an autowired setter method, otherwise false
     */
    public static boolean isAutowiredSetterMethod(Method method) {
        return method.isAnnotationPresent(Autowired.class) && method.getName().startsWith(SET_METHOD_START_PREFIX);
    }

    /**
     * Retrieves the instance of a class by invoking the default (parameterless) constructor.
     *
     * @param clazz The class for which an instance should be created
     * @return An instance of the specified class
     * @throws BeanPostProcessorConstructionLimitationException If the default constructor is not present or accessible
     */
    public static Object getConstructorWithOutParameters(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception ex) {
            throw new BeanPostProcessorConstructionLimitationException(
                    String.format(BEAN_SHOULD_HAVE_DEFAULT_CONSTRUCTOR_MESSAGE, clazz.getSimpleName()));
        }
    }

    /**
     * Retrieves the instance of a class by invoking a constructor with a single parameter.
     *
     * @param clazz         The class for which an instance should be created
     * @param parameterType The type of the constructor's single parameter
     * @param instance      The instance to be passed as the constructor's argument
     * @return An instance of the specified class created using the provided parameter
     * @throws BeanPostProcessorConstructionLimitationException If the constructor with a single parameter is not present or accessible
     */
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

    /**
     * Retrieves the instance of a class by invoking a constructor with multiple parameters.
     *
     * @param clazz                    The class for which an instance should be created
     * @param parameterTypesToInstance A map containing parameter types and their corresponding instances
     * @return An instance of the specified class created using the provided parameters
     * @throws BeanPostProcessorConstructionLimitationException If the constructor with specified parameters is not present or accessible
     */
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

    /**
     * Sets a field's value within an object.
     *
     * @param field The field to be modified
     * @param obj   The object containing the field
     * @param value The value to be set in the field
     */
    @SneakyThrows
    public static void setField(Field field, Object obj, Object value) {
        log.trace("Setting into field \"{}\" of {} the value {}", field.getName(), obj, value);
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new BringGeneralException(e);
        }
    }

    /**
     * Retrieves parameter names of a method or constructor.
     *
     * @param methodOrConstructor The method or constructor to retrieve parameter names from
     * @return A list of parameter names
     */
    public static List<String> getParameterNames(AccessibleObject methodOrConstructor) {
        return Arrays.stream(info.lookupParameterNames(methodOrConstructor)).toList();
    }

    /**
     * Extracts the position of a parameter.
     *
     * @param parameter The parameter to extract the position from
     * @return The position of the parameter
     */
    public static int extractParameterPosition(Parameter parameter) {
        String name = parameter.getName();
        return Integer.parseInt(name.substring(name.indexOf(ARG) + ARG.length()));
    }

    /**
     * Extracts implementation classes of a generic type.
     *
     * @param genericType            The generic type
     * @param reflections            The Reflections object to query types
     * @param createdBeanAnnotations List of annotations indicating created beans
     * @return A list of implementation classes
     */
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

    /**
     * Extracts implementation classes of a given type.
     *
     * @param type                    The type to extract implementations for
     * @param reflections             The Reflections object to query types
     * @param createdBeanAnnotations  List of annotations indicating created beans
     * @return A list of implementation classes
     */
    public static  List<Class<?>> extractImplClasses(Class<?> type, Reflections reflections,
        List<Class<? extends Annotation>> createdBeanAnnotations) {
        return (List<Class<?>>)reflections.getSubTypesOf(type)
            .stream()
            .filter(implementation -> isImplementationAnnotated(implementation, createdBeanAnnotations))
            .toList();
    }

    /**
     * Checks if an implementation class is annotated with any of the specified annotations.
     *
     * @param implementation         The implementation class to check
     * @param createdBeanAnnotations List of annotations indicating created beans
     * @return True if the implementation is annotated, otherwise false
     */
    private static boolean isImplementationAnnotated(Class<?> implementation,
                                                     List<Class<? extends Annotation>> createdBeanAnnotations) {
        return Arrays.stream(implementation.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(createdBeanAnnotations::contains);
    }

    /**
     * Invokes a method on an object and returns a Supplier for its result.
     *
     * @param method The method to invoke
     * @param obj    The object to invoke the method on
     * @param params The parameters to pass to the method
     * @return A Supplier representing the method invocation
     */
    public static Supplier<Object> invokeBeanMethod(Method method, Object obj, Object[] params) {
        return () -> {
            try {
                return method.invoke(obj, params);
            } catch (Exception e) {
                throw new BringGeneralException(e);
            }
        };
    }

    /**
     * Creates a new instance using the given constructor and arguments and returns a Supplier for it.
     *
     * @param constructor The constructor to create the instance
     * @param args        The arguments to pass to the constructor
     * @param clazz       The class of the instance to be created
     * @param proxy       Boolean flag indicating whether to use proxy creation
     * @return A Supplier representing the new instance creation
     */
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

    /**
     * Processes the annotations on the methods of a bean.
     *
     * @param bean       The bean object
     * @param declaredMethods The methods of the bean to process
     * @param annotation The annotation to process
     * @throws ReflectiveOperationException If an error occurs during reflective operations
     */
    public static void processBeanPostProcessorAnnotation(Object bean,
                                                          Method[] declaredMethods,
                                                          Class<? extends Annotation> annotation)
            throws ReflectiveOperationException {
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(annotation)) {
                declaredMethod.invoke(bean);
            }
        }
    }

    /**
     * Inner class extending AnnotationParanamer to handle Qualifier annotations.
     */
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
