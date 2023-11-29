package com.bobocode.bring.core.context.type;

import static com.bobocode.bring.core.utils.ReflectionUtils.extractImplClasses;

import com.bobocode.bring.core.annotation.Qualifier;
import com.bobocode.bring.core.context.impl.AnnotationBringBeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.List;
import org.reflections.Reflections;

public class ParameterInterfaceValueTypeInjector extends AbstractValueTypeInjector implements ParameterValueTypeInjector {

  private final Reflections reflections;

  protected ParameterInterfaceValueTypeInjector(Reflections reflections, AnnotationBringBeanRegistry beanRegistry,
      ClassPathScannerFactory classPathScannerFactory) {
    super(beanRegistry, classPathScannerFactory);
    this.reflections = reflections;
  }

  @Override
  public boolean hasAnnotatedWithValue(Parameter parameter) {
    return parameter.getType().isInterface() && !Collection.class.isAssignableFrom(parameter.getType());
  }

  @Override
  public Object setValueToSetter(Parameter parameter, String parameterName, List<Class<? extends Annotation>> createdBeanAnnotations) {
    var implementations = extractImplClasses(parameter.getType(), reflections, createdBeanAnnotations);
    String qualifier = parameter.isAnnotationPresent(Qualifier.class) ? parameter.getAnnotation(Qualifier.class).value() : null;
    return findImplementationByPrimaryOrQualifier(implementations, parameter.getType(), qualifier, parameterName);
  }
}
