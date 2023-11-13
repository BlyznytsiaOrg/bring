package com.bobocode.bring.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> If the annotation {@code @Configuration} is present on the declaration of a class,
 * then it indicates that this class is a source of bean definitions.
 * <p> Beans under a configuration class are created by adding methods and annotating them with annotation {@code @Bean}.
 * 
 *  @author Blyzhnytsia Team
 *  @since 1.0
 *  @see Bean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Configuration {
}
