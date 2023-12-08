package io.github.blyznytsiaorg.bring.web.servlet.mapping.request;

import io.github.blyznytsiaorg.bring.web.servlet.annotation.PathVariable;
import io.github.blyznytsiaorg.bring.web.servlet.mapping.RestControllerParams;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * The RequestParamsResolver interface provides methods for resolving request parameters
 * in the web context of a {@code @RestController}.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public interface RequestParamsResolver {

    /**
     * Retrieves the annotation class associated with the resolver.
     *
     * @return The annotation class.
     */
    Class<? extends Annotation> getAnnotation();

    /**
     * Handles the annotation on a method and processes the associated parameters.
     *
     * @param instance                 The instance of the controller.
     * @param method                   The method with the annotation.
     * @param requestMappingPath       The path specified in an annotation.
     * @param restControllerParamsMap  A map containing controller paths and associated parameters.
     */
    void handleAnnotation(Object instance, Method method, String requestMappingPath,
                          Map<String, List<RestControllerParams>> restControllerParamsMap);

    /**
     * Extracts the method path from the provided path by removing path variables.
     *
     * @param path The original method path.
     * @return The method path without path variables.
     */
    default String getMethodPath(String path) {
        if (path.contains("{") && path.contains("}")) {
            int index = path.lastIndexOf("{");
            path = path.substring(0, index);
        }
        return path;
    }

    /**
     * Adds a RestControllerParams object to the list in a sorted manner
     * based on the presence of PathVariable.
     *
     * @param restControllerParams The RestControllerParams object to be added.
     * @param methodParamsList     The list of RestControllerParams objects associated with a method.
     */
    default void addSorted(RestControllerParams restControllerParams,
                           List<RestControllerParams> methodParamsList) {
        Parameter[] parameters = restControllerParams.method().getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(PathVariable.class)) {
                methodParamsList.add(restControllerParams);
                return;
            }
        }
        methodParamsList.add(0, restControllerParams);
    }
}
