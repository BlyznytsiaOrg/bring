package com.bobocode.bring.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that a method will be used to create {@code Singleton} or {@code Prototype} Beans.
 * The result of the method invocation is an object that represents a Bean that can be injected into other Beans
 * via constructor, field injection or setter using {@code Autowired} annotation.
 *
 * <p>This annotation should be used only for methods under a configuration class
 * (a class annotated with {@code @Configuration}). When applied to a method,
 * it becomes eligible for Bean definition registration and later used for Bean creation.
 *
 * <p>The name of the Bean will be the name of the method. Injection of other Bean via parameter is possible by
 * adding a method parameter of the type and name of a bean already defined in the Configuration class.
 *
 * <p><strong>Usage Example:</strong>
 *
 * <pre>{@code
 * @Configuration
 * public class MyConfiguration {
 *
 *     @Bean
 *     public String stringBean1() {
 *         return "Hello, 1";
 *     }
 *
 *     @Bean
 *     public String stringBean2(String stringBean1) {
 *         return stringBean1 + "!";
 *     }
 * }
 * }</pre>
 *
 * @author Blyzhnytsia Team
 * @see Configuration
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bean {
    /**
     * The name of the Bring bean. If not specified, the bean name will be generated based on the method name.
     *
     * @return the name of the Bring bean
     */
    String value() default "";
}
