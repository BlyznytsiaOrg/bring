package io.github.blyznytsiaorg.bring.core.context.type.parameter;

import static io.github.blyznytsiaorg.bring.core.utils.ReflectionUtils.extractImplClasses;

import io.github.blyznytsiaorg.bring.core.annotation.Qualifier;

import io.github.blyznytsiaorg.bring.core.context.impl.AnnotationBringBeanRegistry;
import io.github.blyznytsiaorg.bring.core.context.scaner.ClassPathScannerFactory;
import io.github.blyznytsiaorg.bring.core.context.type.AbstractValueTypeInjector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.List;
import io.github.blyznytsiaorg.bring.core.utils.ReflectionUtils;
import org.reflections.Reflections;

/**
 * Handles injection of values into parameters that are interfaces and not assignable from java.util.Collection.
 * This class extends AbstractValueTypeInjector and implements ParameterValueTypeInjector.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class ParameterInterfaceValueTypeInjector extends AbstractValueTypeInjector implements ParameterValueTypeInjector {

  private final Reflections reflections;

  /**
   * Constructs a ParameterInterfaceValueTypeInjector.
   *
   * @param reflections             The Reflections object used for scanning and retrieving types.
   * @param beanRegistry            The AnnotationBringBeanRegistry responsible for bean management.
   * @param classPathScannerFactory The ClassPathScannerFactory for scanning classes in the classpath.
   */
  public ParameterInterfaceValueTypeInjector(Reflections reflections, AnnotationBringBeanRegistry beanRegistry,
      ClassPathScannerFactory classPathScannerFactory) {
    super(beanRegistry, classPathScannerFactory);
    this.reflections = reflections;
  }

  /**
   * Checks if the provided parameter is an interface and is not assignable from java.util.Collection.
   *
   * @param parameter The parameter to check for annotations.
   * @return True if the parameter is an interface and not assignable from java.util.Collection; otherwise, false.
   */
  @Override
  public boolean hasAnnotatedWithValue(Parameter parameter) {
    return parameter.getType().isInterface() && !Collection.class.isAssignableFrom(parameter.getType());
  }

  /**
   * Sets the value to the specified parameter in the associated setter method based on certain conditions.
   *
   * @param parameter                The parameter where the value is to be set.
   * @param parameterName            The name of the parameter.
   * @param createdBeanAnnotations   List of annotations associated with created beans.
   * @return The value set to the parameter in the setter method.
   */
  @Override
  public Object setValueToSetter(Parameter parameter, String parameterName, List<Class<? extends Annotation>> createdBeanAnnotations) {
    var implementations = ReflectionUtils.extractImplClasses(parameter.getType(), reflections, createdBeanAnnotations);
    String qualifier = parameter.isAnnotationPresent(Qualifier.class) ? parameter.getAnnotation(Qualifier.class).value() : null;
    return findImplementationByPrimaryOrQualifier(implementations, parameter.getType(), qualifier, parameterName);
  }
}
