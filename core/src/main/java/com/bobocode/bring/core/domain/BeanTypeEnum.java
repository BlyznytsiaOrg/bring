package com.bobocode.bring.core.domain;

import com.bobocode.bring.core.annotation.*;
import com.bobocode.bring.core.exception.BeanAnnotationMissingException;
import com.bobocode.bring.core.utils.Pair;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Enumeration representing different types of beans in an application based on their annotations.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Getter
public enum BeanTypeEnum {
    
    CONFIGURATION(1, Configuration.class),
    
    CONFIGURATION_BEAN(2, Bean.class),

    SERVICE(3, Service.class),

    COMPONENT(3, Component.class),

    UNDEFINED(4);

    private final int order;
    
    private final List<Class<?>> annotationClasses;

    /**
     * Constructor to initialize BeanTypeEnum with order and associated annotation classes.
     *
     * @param order             The order of the bean type
     * @param annotationClasses The associated annotation classes
     */
    BeanTypeEnum(int order, Class<?>... annotationClasses) {
        this.order = order;
        this.annotationClasses = List.of(annotationClasses);
    }

    /**
     * Finds the BeanTypeEnum based on the annotations present on a given class.
     *
     * @param clazz The class to find the bean type for
     * @return The BeanTypeEnum associated with the class annotations, or UNDEFINED if not found
     */
    public static BeanTypeEnum findBeanType(Class<?> clazz) {
        Map<Class<?>, BeanTypeEnum> annotationToBeanType = getAnnotationToBeanType();
        
        return Arrays.stream(clazz.getAnnotations())
                .map(annotation -> annotationToBeanType.get(annotation.annotationType()))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(BeanTypeEnum.UNDEFINED);
    }

    /**
     * Finds the BeanTypeEnum based on the annotations present on a given method.
     *
     * @param method The method to find the bean type for
     * @return The BeanTypeEnum associated with the method annotations
     * @throws BeanAnnotationMissingException If the method is not annotated with @Bean annotation
     */
    public static BeanTypeEnum findBeanType(Method method) {
        Map<Class<?>, BeanTypeEnum> annotationToBeanType = getAnnotationToBeanType();

        return Arrays.stream(method.getAnnotations())
            .map(annotation -> annotationToBeanType.get(annotation.annotationType()))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(() -> new BeanAnnotationMissingException(
                "Unable to create Bean of type=[%s], methodName=[%s]. Method is not annotated with @Bean annotation",
                method.getReturnType(), 
                method.getName()));
    }

    /**
     * Retrieves a map of annotation classes to their corresponding BeanTypeEnum.
     *
     * @return A map containing annotation classes as keys and their associated BeanTypeEnum as values
     */
    private static Map<Class<?>, BeanTypeEnum> getAnnotationToBeanType() {
        return Arrays.stream(values())
            .flatMap(beanType -> beanType.getAnnotationClasses()
                .stream()
                .map(annotation -> Pair.of(annotation, beanType)))
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }
    
}
