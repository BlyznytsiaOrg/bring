package io.github.blyznytsiaorg.bring.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that an annotated class is a Bring component. This annotation serves as a marker for Bring to
 * automatically detect and register the annotated class as a bean during component scanning.
 *
 * <p>By default, the name of the bean will be the simple name of the annotated class. You can customize the bean name
 * by providing a value to the {@code value} attribute. If no value is provided, the bean name will be generated based
 * on the class name with the initial letter in lowercase.
 *
 * <p>This annotation is part of the Bring Framework's component scanning mechanism, which allows for automatic
 * discovery and registration of Bring beans.
 *
 * <p><strong>Usage Example:</strong>
 * <pre>
 * {@code
 *  @Service
 *  public class MyComponent {
 *      // Class definition
 *  }
 *
 *  // Custom bean name
 *  @Service("customBeanName")
 *  public class AnotherComponent {
 *      // Class definition
 *  }
 * }
 * </pre>
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
    /**
     * The name of the Bring bean. If not specified, the bean name will be generated based on the class name.
     *
     * @return the name of the Bring bean
     */
    String value() default "";
}
