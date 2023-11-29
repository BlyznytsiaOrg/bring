package com.bobocode.bring.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to qualify the selection of a bean when multiple beans of the same type
 * are available for autowiring or injection.
 * <p>
 * This annotation can be applied to fields, methods, parameters, types, or other annotations
 * to specify the qualifier value, aiding in the selection of the exact bean to be injected or autowired.
 * </p>
 * <p>
 * The {@code value} attribute within {@code @Qualifier} allows for providing a specific identifier
 * or qualifier string to differentiate between multiple beans of the same type. When used in conjunction
 * with dependency injection bring frameworks, it helps in precisely identifying the intended bean to be injected.
 * </p>
 *
 * @since 1.0
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualifier {

  /**
   * Specifies the qualifier value to differentiate between multiple beans of the same type.
   *
   * @return A string value representing the qualifier or identifier for bean selection.
   */
  String value() default "";

}
