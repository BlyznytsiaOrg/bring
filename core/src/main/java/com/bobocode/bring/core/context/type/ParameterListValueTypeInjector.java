package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.context.impl.AnnotationBringBeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.bobocode.bring.core.utils.ReflectionUtils.extractImplClasses;

public class ParameterListValueTypeInjector extends AbstractValueTypeInjector implements ParameterValueTypeInjector{

    private final Reflections reflections;

    protected ParameterListValueTypeInjector(Reflections reflections, AnnotationBringBeanRegistry beanRegistry,
                                             ClassPathScannerFactory classPathScannerFactory) {
        super(beanRegistry, classPathScannerFactory);
        this.reflections = reflections;
    }

    @Override
    public boolean hasAnnotatedWithValue(Parameter parameter) {
        return Collection.class.isAssignableFrom(parameter.getType());
    }

    @Override
    public Object setValueToSetter(Parameter parameter, List<Class<? extends Annotation>> createdBeanAnnotations) {
        ParameterizedType genericTypeOfField = (ParameterizedType) parameter.getParameterizedType();
        List<Class<?>> dependencies = extractImplClasses(genericTypeOfField, reflections, createdBeanAnnotations);
        return injectListDependency(dependencies);
    }
}
