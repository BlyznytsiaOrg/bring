package com.bobocode.bring.web.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to indicate that a method parameter should be bound to the body of
 * the HTTP request.
 *
 * <p>
 * This annotation does not have additional attributes when applied to a method parameter.
 * </p>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestBody {
}
