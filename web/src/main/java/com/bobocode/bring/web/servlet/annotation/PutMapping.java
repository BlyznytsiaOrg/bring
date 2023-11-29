package com.bobocode.bring.web.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping HTTP {@code PUT} requests onto specific handler
 * methods.
 *
 * @see GetMapping
 * @see PostMapping
 * @see DeleteMapping
 * @see PatchMapping
 * @see com.bobocode.bring.web.servlet.http.ResponseEntity
 *
 * <p><strong>Usage Example:</strong>
 * <pre>
 * {@code
 * @RestController
 * public class MyRestController implements BringServlet {
 *
 *      @PutMapping(path = "/resource/{id}")
 *      public void updateResource(@PathVariable Lond id, @RequestBody UserDto dto) {
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
 *      @PutMapping(path = "/resource")
 *      public void putResource(@RequestBody UserDto dto) {
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
public @interface PutMapping {

    /**
     * The URL path or pattern for mapping HTTP PUT requests to the annotated method.
     *
     * @return the URL path or pattern for PUT requests
     */
    String path() default "";
}
