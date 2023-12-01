package com.bobocode.bring.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that a field, constructor, or method should be autowired by the Bring IoC container.
 * This annotation can be applied to fields, constructors, and methods in a Bring bean class to let Bring automatically
 * inject the dependencies at runtime.
 *
 * When applied to a field, the Bring IoC container will automatically inject a compatible bean, resolved by type,
 * into the annotated field. When applied to a constructor, the container will use constructor injection to provide
 * the required dependencies. If applied to a method, the container will invoke the method after initializing the bean,
 * injecting the necessary dependencies into the method parameters.
 *
 * This annotation is part of the Bring Framework's dependency injection mechanism, enabling the creation of loosely
 * coupled and easily testable components.
 *
 * When we have a class possesses only one constructor, explicitly adding the @Autowired annotation isn't mandatory.
 * Bring inherently knows which constructor to invoke and handles it accordingly.
 *
 * For classes with multiple constructors, it becomes imperative to specify to Bring which constructor should be utilized
 * through the @Autowired annotation. This annotation acts as a directive for Bing to identify
 * and use the designated constructor to resolve dependencies properly.
 * This way, when Bring encounters multiple constructors within a class, the @Autowired annotation guides it in selecting the appropriate constructor for dependency resolution.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}
