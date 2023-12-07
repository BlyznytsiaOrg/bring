package io.github.blyznytsiaorg.bring.web.utils;

import io.github.blyznytsiaorg.bring.web.servlet.BringServlet;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestMapping;
import io.github.blyznytsiaorg.bring.web.servlet.mapping.request.RequestParamsResolver;
import io.github.blyznytsiaorg.bring.web.servlet.mapping.RestControllerParams;
import lombok.experimental.UtilityClass;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code RestControllerParamsUtil} class is a utility class providing methods for extracting
 * {@link RestControllerParams} information from a {@link BringServlet} using a list of
 * {@link RequestParamsResolver}s.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@UtilityClass
public final class RestControllerParamsUtil {

    /**
     * Retrieves the mapping of controller methods to their associated {@link RestControllerParams}.
     *
     * @param bringServlet           The instance of the {@link BringServlet} containing the controller methods.
     * @param requestParamsResolvers The list of {@link RequestParamsResolver}s used for method annotation handling.
     * @return A map where keys are HTTP method names and values are lists of {@link RestControllerParams}.
     */
    public static Map<String, List<RestControllerParams>> getRestControllerParams(
            BringServlet bringServlet,
            List<RequestParamsResolver> requestParamsResolvers) {
        Class<? extends BringServlet> clazz = bringServlet.getClass();
        Map<String, List<RestControllerParams>> restConrollerParamsMap = new HashMap<>();
        String requestMappingPath;
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            requestMappingPath = clazz.getAnnotation(RequestMapping.class).path();
        } else {
            requestMappingPath = "";
        }

        // Process each method in the controller class
        for (Method method : clazz.getMethods()) {

            // Find a resolver for the method's annotation and handle it if present
            requestParamsResolvers.stream()
                    .filter(resolver -> method.isAnnotationPresent(resolver.getAnnotation()))
                    .findFirst()
                    .ifPresent(resolver -> resolver.handleAnnotation(bringServlet, method, requestMappingPath,
                            restConrollerParamsMap));
        }
        return restConrollerParamsMap;
    }
}

