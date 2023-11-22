package com.bobocode.bring.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated class is a controller.
 * Controllers typically handle incoming requests, perform necessary processing, and provide a response.
 * This annotation can be used to mark classes as controllers within a framework or application.
 * <p>
 * Example:
 * <pre>{@code
 * {@literal @}Controller
 * public class MyController {
 *     // Controller logic and methods
 * }
 * }</pre>
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
}
