package com.bobocode.bring.core.utils;

import com.thoughtworks.paranamer.AnnotationParanamer;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@UtilityClass
public final class ReflectionUtils {

    private final Paranamer info = new CachingParanamer(new AnnotationParanamer(new BytecodeReadingParanamer()));
    private static final String ARG = "arg";

    @SneakyThrows
    public static void setField(Field field, Object obj, Object value) {
        field.setAccessible(true);
        field.set(obj, value);
    }
    
    public List<String> getParameterNames(AccessibleObject methodOrConstructor) {
        return Arrays.stream(info.lookupParameterNames(methodOrConstructor)).toList();
    }
    
    public int extractParameterPosition(Parameter parameter) {
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

                return (List<Class<?>>) reflections.getSubTypesOf(interfaceClass)
                        .stream()
                        .filter(implementation -> isImplementationAnnotated(implementation, createdBeanAnnotations))
                        .toList();
            }
            return Collections.emptyList();
        }

    private static boolean isImplementationAnnotated(Class<?> implementation, List<Class<? extends Annotation>> createdBeanAnnotations) {
        return Arrays.stream(implementation.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(createdBeanAnnotations::contains);
    }
}
