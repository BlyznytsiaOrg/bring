package com.bobocode.bring.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Bean} annotation applied to a method indicates that an Object of type the return type of the method 
 * will be added in the ApplicationContext. The ApplicationContext represents a Map were all those objects are stored
 * and can be used for Dependency injection. The definition of those Objects is defined in the methods bodies.
 * 
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bean {
}
