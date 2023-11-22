package com.bobocode.bring.core.context.type;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import static com.bobocode.bring.core.utils.ReflectionUtils.extractImplClasses;

public class ParameterListValueTypeInjector implements ParameterValueTypeInjector {

    private final Reflections reflections;

    public ParameterListValueTypeInjector(Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public boolean hasAnnotatedWithValue(Parameter parameter) {
        return Collection.class.isAssignableFrom(parameter.getType());
    }

    @Override
    public Object setValueToSetter(Parameter parameter, List<Class<? extends Annotation>> createdBeanAnnotations) {
        ParameterizedType genericTypeOfField = (ParameterizedType) parameter.getParameterizedType();
        return extractImplClasses(genericTypeOfField, reflections, createdBeanAnnotations);
    }
}
