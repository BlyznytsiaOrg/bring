package io.github.blyznytsiaorg.bring.web.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated class is a specialized type of controller designed for RESTful services.
 * REST controllers typically handle HTTP requests, process them, and generate HTTP responses suitable for REST APIs.
 * This annotation can be used to mark classes as REST controllers within a framework or application.
 *
 *  <p><strong>Usage Example:</strong>
 * <pre>
 * {@code
 * @RestController
 * public class MyRestController implements BringServlet {
 *     // REST controller logic and methods
 * }
 * }</pre>
 *
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestController {
}
