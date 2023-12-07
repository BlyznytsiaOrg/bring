package io.github.blyznytsiaorg.bring.web.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to indicate the mapping of a request to a specific controller class.
 * This annotation is applied at the class level to define the base path or URL pattern
 * for all handler methods within the controller.
 *
 * <p><strong>Usage Example:</strong>
 * <pre>
 * {@code
 * @RestController
 * @RequestMapping(path = "/example")
 * public class MyRestController implements BringServlet {
 *
 *        // Your implementation logic here
 *    }
 *  }
 * </pre>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    /**
     * The URL path or pattern for mapping HTTP requests to the annotated class.
     * If not specified, the controller will respond to requests for the base path
     * or URL pattern.
     *
     * @return the URL path or pattern for requests to the annotated class
     */
    String path() default "";
}
