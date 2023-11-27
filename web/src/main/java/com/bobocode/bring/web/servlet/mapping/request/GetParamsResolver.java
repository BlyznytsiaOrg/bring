package com.bobocode.bring.web.servlet.mapping.request;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.web.servlet.annotation.GetMapping;
import com.bobocode.bring.web.servlet.annotation.RequestMethod;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The GetParamsResolver class implements the RequestParamsResolver interface
 * to handle the @GetMapping annotation.
 *
 * @see GetMapping
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Component
public class GetParamsResolver
        implements RequestParamsResolver {

    /**
     * Retrieves the @GetMapping annotation class.
     *
     * @return The @GetMapping annotation class.
     */
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GetMapping.class;
    }

    /**
     * Handles the @GetMapping annotation on a method and processes the associated parameters.
     *
     * @param instance                 The instance of the controller.
     * @param method                   The method with the @GetMapping annotation.
     * @param requestMappingPath       The request mapping path.
     * @param restControllerParamsMap  A map containing controller paths and associated parameters.
     */
    @Override
    public void handleAnnotation(Object instance, Method method, String requestMappingPath,
                                 Map<String, List<RestControllerParams>> restControllerParamsMap) {
        GetMapping annotation = method.getAnnotation(GetMapping.class);
        String methodPath = getMethodPath(annotation.path());
        String path = requestMappingPath + methodPath;
        RestControllerParams params = new RestControllerParams(instance, method,
                RequestMethod.GET, path);
        List<RestControllerParams> getMethodParamsList = Optional.ofNullable(
                restControllerParamsMap.get(RequestMethod.GET.name()))
                .orElse(new ArrayList<>());
        addSorted(params, getMethodParamsList);
        restControllerParamsMap.put(RequestMethod.GET.name(), getMethodParamsList);
    }
}
