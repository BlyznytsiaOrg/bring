package io.github.blyznytsiaorg.bring.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>The {@code PostConstruct} annotation is used on a method that needs
 * to be executed after dependency injection is done to perform any
 * initialization. This method is called immediately after the bean's
 * properties have been set and the bean has been placed into the
 * Bring container.</p>
 *
 * <p>Methods annotated with {@code @PostConstruct} are invoked only once
 * in the bean's lifecycle, and they provide a convenient way to
 * initialize resources or perform any setup logic that is required
 * before the bean is ready for use.</p>
 *
 * <p>The method annotated with {@code @PostConstruct} must be non-static
 * and should not have any parameters, as it is meant to be an
 * initialization callback method for the bean instance. If multiple
 * methods are annotated with {@code @PostConstruct} within a single
 * class, the order of execution is not guaranteed.</p>
 *
 * <p>Example:</p>
 * <pre>
 * {@code
 * import com.bobocode.bring.core.annotation.PostConstruct;
 *
 * public class ExampleBean {
 *
 *     private String message;
 *
 *     @PostConstruct
 *     public void init() {
 *         message = "Hello, this is an example!";
 *         // Additional initialization logic
 *     }
 *
 *     public String getMessage() {
 *         return message;
 *     }
 * }
 * }
 * </pre>
 *
 * <p>In this example, the {@code init} method will be automatically
 * invoked after the {@code ExampleBean} is constructed, providing a
 * way to perform custom initialization logic.</p>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostConstruct {
}
