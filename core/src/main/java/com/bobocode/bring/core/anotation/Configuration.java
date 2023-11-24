package com.bobocode.bring.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that a class is a source of bean definitions. Those bean definitions are registered during
 * BeanFactoryPostProcessors invocation. The factory post processor dedicated for Configyration classes is
 * {@code ConfigurationClassPostProcessor}
 * <p>Beans under a configuration class are created by adding methods and annotating them with annotation {@code @Bean}.
 *
 * <p>Configuration bean must be of Singleton scope and is registered before the beans defined inside of it.
 *
 * <p><strong>Usage Example:</strong>
 *
 * <pre>{@code
 * @Configuration
 * public class MyConfiguration {
 *
 * }
 * }</pre>
 *
 * @author Blyzhnytsia Team
 * @see Bean
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Configuration {
}
