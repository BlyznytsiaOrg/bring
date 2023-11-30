package com.bobocode.bring.core.context.type.field;

import static com.bobocode.bring.core.utils.ReflectionUtils.extractImplClasses;

import com.bobocode.bring.core.annotation.Qualifier;

import com.bobocode.bring.core.context.impl.AnnotationBringBeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.context.type.AbstractValueTypeInjector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import org.reflections.Reflections;

public class FieldInterfaceValueTypeInjector extends AbstractValueTypeInjector implements FieldValueTypeInjector {

  private final Reflections reflections;

  public FieldInterfaceValueTypeInjector(Reflections reflections, AnnotationBringBeanRegistry beanRegistry,
      ClassPathScannerFactory classPathScannerFactory) {
    super(beanRegistry, classPathScannerFactory);
    this.reflections = reflections;
  }

  @Override
  public boolean hasAnnotatedWithValue(Field field) {
    return field.getType().isInterface() && !Collection.class.isAssignableFrom(field.getType());
  }

  @Override
  public Object setValueToField(Field field, Object bean, List<Class<? extends Annotation>> createdBeanAnnotations) {
    var implementations = extractImplClasses(field.getType(), reflections, createdBeanAnnotations);
    String qualifier = field.isAnnotationPresent(Qualifier.class) ? field.getAnnotation(Qualifier.class).value() : null;
    return findImplementationByPrimaryOrQualifier(implementations, field.getType(), qualifier, field.getName());
  }

}
