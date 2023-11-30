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

/**
 * Handles injection of values into fields that are interfaces and not assignable from java.util.Collection.
 * This class extends AbstractValueTypeInjector and implements FieldValueTypeInjector.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
public class FieldInterfaceValueTypeInjector extends AbstractValueTypeInjector implements FieldValueTypeInjector {

  private final Reflections reflections;

  /**
   * Constructs a FieldInterfaceValueTypeInjector.
   *
   * @param reflections             The Reflections object used for scanning and retrieving types.
   * @param beanRegistry            The AnnotationBringBeanRegistry responsible for bean management.
   * @param classPathScannerFactory The ClassPathScannerFactory for scanning classes in the classpath.
   */
  public FieldInterfaceValueTypeInjector(Reflections reflections, AnnotationBringBeanRegistry beanRegistry,
      ClassPathScannerFactory classPathScannerFactory) {
    super(beanRegistry, classPathScannerFactory);
    this.reflections = reflections;
  }

  /**
   * Checks if the provided field is an interface and is not assignable from java.util.Collection.
   *
   * @param field The field to check for annotations.
   * @return True if the field is an interface and not assignable from java.util.Collection; otherwise, false.
   */
  @Override
  public boolean hasAnnotatedWithValue(Field field) {
    return field.getType().isInterface() && !Collection.class.isAssignableFrom(field.getType());
  }

  /**
   * Sets the value to the specified field in the provided bean based on certain conditions.
   *
   * @param field                    The field where the value is to be set.
   * @param bean                     The bean instance where the value is to be set.
   * @param createdBeanAnnotations   List of annotations associated with created beans.
   * @return The value set to the field in the bean.
   */
  @Override
  public Object setValueToField(Field field, Object bean, List<Class<? extends Annotation>> createdBeanAnnotations) {
    var implementations = extractImplClasses(field.getType(), reflections, createdBeanAnnotations);
    String qualifier = field.isAnnotationPresent(Qualifier.class) ? field.getAnnotation(Qualifier.class).value() : null;
    return findImplementationByPrimaryOrQualifier(implementations, field.getType(), qualifier, field.getName());
  }

}
