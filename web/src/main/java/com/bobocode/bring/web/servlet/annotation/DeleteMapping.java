package com.bobocode.bring.web.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping HTTP {@code DELETE} requests onto specific handler
 * methods.
 *
 * <p><strong>Usage Example:</strong>
 * <pre>
 * {@code
 * @RestController
 * public class MyRestController implements BringServlet {
 *
 *      @DeleteMapping(path = "/resource/{id}")
 *      public void deleteResource(@PathVariable Long id) {
 *          // Your implementation logic here
 *      }
 *    }
 *  }
 * </pre>
 *
 * @see GetMapping
 * @see PostMapping
 * @see PutMapping
 * @see PatchMapping
 * @see com.bobocode.bring.web.servlet.http.ResponseEntity
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DeleteMapping {

    /**
     * The URL path or pattern for mapping HTTP DELETE requests to the annotated method.
     *
     * @return the URL path or pattern for DELETE requests
     */
    String path() default "";
}
