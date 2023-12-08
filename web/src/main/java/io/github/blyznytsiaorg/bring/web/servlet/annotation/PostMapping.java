package io.github.blyznytsiaorg.bring.web.servlet.annotation;

import io.github.blyznytsiaorg.bring.web.servlet.http.ResponseEntity;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping HTTP {@code POST} requests onto specific handler
 * methods.
 *
 * @see GetMapping
 * @see DeleteMapping
 * @see PutMapping
 * @see PatchMapping
 * @see ResponseEntity
 *
 * <p><strong>Usage Example:</strong>
 * <pre>
 * {@code
 * @RestController
 * public class MyRestController implements BringServlet {
 *
 *      @PostMapping(path = "/resource")
 *      public void saveResource(@RequestBody UserDto dto) {
 *          // Your implementation logic here
 *      }
 *    }
 *  }
 * </pre>
 *
 * <p><strong>Usage Example:</strong>
 * <pre>
 * {@code
 * @RestController
 * public class MyController implements BringServlet {
 *
 *      @PostMapping(path = "/resource")
 *      public void postResource(@RequestBody UserDto dto) {
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
@Target(ElementType.METHOD)
public @interface PostMapping {

    /**
     * The URL path or pattern for mapping HTTP POST requests to the annotated method.
     *
     * @return the URL path or pattern for POST requests
     */
    String path() default "";
}
