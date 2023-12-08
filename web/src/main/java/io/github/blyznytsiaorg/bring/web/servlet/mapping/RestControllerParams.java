package io.github.blyznytsiaorg.bring.web.servlet.mapping;

import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestMethod;
import java.lang.reflect.Method;

/**
 * The {@code RestControllerParams} record represents parameters associated with a method
 * in a web controller.
 *<p>
 * This record is used to encapsulate information such as the controller instance,
 * the method, the HTTP request method, and the path.
 *</p>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 *
 * @param instance       The instance of the controller.
 * @param method         The method associated with the parameters.
 * @param requestMethod  The HTTP request method associated with the method.
 * @param path           The path associated with the method.
 */
public record RestControllerParams(Object instance, Method method, RequestMethod requestMethod, String path) {
}
