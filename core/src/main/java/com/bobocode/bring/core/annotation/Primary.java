package com.bobocode.bring.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a bean marked with this annotation is the primary candidate
 * when multiple beans of the same type are available for autowiring or injection.
 * <p>
 * This annotation can be applied to a class or a method within a class to specify
 * it as the primary choice for dependency resolution.
 * </p>
 * <p>
 * When multiple beans of the same type are available, the one annotated with {@code @Primary}
 * will be given precedence for injection or autowiring. It serves as the default choice
 * when the bring framework needs to resolve the ambiguity between multiple candidates of the same type.
 * </p>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Primary {

}
