package com.bobocode.bring.web.servlet.mapping.request;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.web.servlet.annotation.DeleteMapping;
import com.bobocode.bring.web.servlet.annotation.RequestMethod;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The DeleteParamsResolver class implements the RequestParamsResolver interface
 * to handle the @DeleteMapping annotation.
 *
 * @see DeleteMapping
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Component
public class DeleteParamsResolver
        implements RequestParamsResolver {

    /**
     * Retrieves the @DeleteMapping annotation class.
     *
     * @return The @DeleteMapping annotation class.
     */
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return DeleteMapping.class;
    }

    /**
     * Handles the @DeleteMapping annotation on a method and processes the associated parameters.
     *
     * @param instance                 The instance of the controller.
     * @param method                   The method with the @DeleteMapping annotation.
     * @param requestMappingPath       The request mapping path.
     * @param restControllerParamsMap  A map containing controller paths and associated parameters.
     */
    @Override
    public void handleAnnotation(Object instance, Method method, String requestMappingPath,
                                 Map<String, List<RestControllerParams>> restControllerParamsMap) {
        DeleteMapping annotation = method.getAnnotation(DeleteMapping.class);
        String methodPath = getMethodPath(annotation.path());
        String path = requestMappingPath + methodPath;
        RestControllerParams params = new RestControllerParams(instance, method, RequestMethod.DELETE, path);
        List<RestControllerParams> getMethodParamsList = Optional.ofNullable(
                        restControllerParamsMap.get(RequestMethod.DELETE.name()))
                .orElse(new ArrayList<>());
        addSorted(params, getMethodParamsList);
        restControllerParamsMap.put(RequestMethod.DELETE.name(), getMethodParamsList);
    }
}
