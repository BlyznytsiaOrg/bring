package io.github.blyznytsiaorg.bring.web.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to indicate that a method parameter should be bound to a specific
 * HTTP request header.
 *
 * <p><strong>Usage Example:</strong>
 * <pre>
 * {@code
 * @RestController
 * public class MyRestController implements BringServlet {
 *
 *      @PostMapping(path = "/resource")
 *      public void resourceHeaders(@RequestHeader("Authorization") String authToken) {
 *          // Your implementation logic here
 *      }
 *    }
 *  }
 * </pre>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestHeader {

    String value() default "";
}
