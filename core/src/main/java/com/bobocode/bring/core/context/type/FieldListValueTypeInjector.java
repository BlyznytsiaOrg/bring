package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.context.impl.AnnotationBringBeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import static com.bobocode.bring.core.utils.ReflectionUtils.extractImplClasses;

public class FieldListValueTypeInjector extends AbstractValueTypeInjector implements FieldValueTypeInjector{

    private final Reflections reflections;

    public FieldListValueTypeInjector(Reflections reflections, AnnotationBringBeanRegistry beanRegistry,
                                      ClassPathScannerFactory classPathScannerFactory) {
        super(beanRegistry, classPathScannerFactory);
        this.reflections = reflections;
    }

    @Override
    public boolean hasAnnotatedWithValue(Field field) {
        return Collection.class.isAssignableFrom(field.getType());
    }

    @Override
    public Object setValueToField(Field field, Object bean, List<Class<? extends Annotation>> createdBeanAnnotations) {
        ParameterizedType genericTypeOfField = (ParameterizedType) field.getGenericType();
        List<Class<?>> dependencies = extractImplClasses(genericTypeOfField, reflections, createdBeanAnnotations);
        return injectListDependency(dependencies);
    }
}