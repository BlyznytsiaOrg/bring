package com.bobocode.bring.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated element is to be resolved to a value.
 * This annotation is typically used to inject values into fields, method parameters,
 * constructor parameters, or method return values in Spring applications.
 * <p>
 * The value provided in this annotation represents the key or expression to resolve
 * from property sources or other value providers.
 * <p>
 * Example usage in a Bring component:
 * <pre>{@code
 * public class MyComponent {
 *     {@literal @}Value("${my.property}")
 *     private String myPropertyValue;
 *
 *     // Methods and logic using myPropertyValue
 * }
 * }</pre>
 *
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {

    String value();
}
