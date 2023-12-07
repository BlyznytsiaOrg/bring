package io.github.blyznytsiaorg.bring.core.annotation;

import io.github.blyznytsiaorg.bring.core.domain.BeanScope;
import io.github.blyznytsiaorg.bring.core.domain.ProxyMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating the visibility of a Bean. It can be applied to a class annotated with
 * {@code Component}, {@code Service} or to a method annotated with {@code Bean}.
 * It has two properties: scope name and proxy mode. The scope name is mandatory when using this annotation
 * and can be {@code Singleton} or {@code Prototype}.
 *
 * <p>Marking a Bean with Singleton scope means that the same object for that bean will be injected/retrieved when required.
 * Marking a Bean with Prototype scope means that every time a new object for that bean will be injected/retrieved when required.
 *
 * <p>There are exceptions to when a Prototype Bean can return the same object: for example when injecting a Prototype Bean
 * into a Singleton Bean. In order to fix it proxy mode can be set to ON.
 *
 * <p>There are two proxy modes: ON and OFF. By default, if not setting explicitly the proxy mode it will be set to "OFF".
 * If the proxy mode it set to ON then a Javassist proxy of the Bean type will be created.
 *
 * <p><strong>Usage Example:</strong>
 *
 * <pre>{@code
 * @Scope(name = BeanScope.PROTOTYPE, proxyMode = ProxyMode.ON)
 * @Component
 * public class MyComponent {
 *
 * }
 * }</pre>
 *
 * @author Blyzhnytsia Team
 * @see Component
 * @see Service
 * @see Bean
 * @see BeanScope
 * @see ProxyMode
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Scope {

    BeanScope name();

    ProxyMode proxyMode() default ProxyMode.OFF;
}
