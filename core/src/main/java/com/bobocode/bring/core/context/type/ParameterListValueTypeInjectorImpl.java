package com.bobocode.bring.core.context.type;

import org.reflections.Reflections;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import static com.bobocode.bring.core.utils.ReflectionUtils.extractImplClasses;

public class ParameterListValueTypeInjectorImpl implements ParameterValueTypeInjector {

    private final Reflections reflections;

    public ParameterListValueTypeInjectorImpl(Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public boolean hasAnnotatedWithValue(Parameter parameter) {
        return Collection.class.isAssignableFrom(parameter.getType());
    }

    @Override
    public Object setValueToSetter(Parameter parameter) {
        ParameterizedType genericTypeOfField = (ParameterizedType) parameter.getParameterizedType();
        return extractImplClasses(genericTypeOfField, reflections);
    }
}
