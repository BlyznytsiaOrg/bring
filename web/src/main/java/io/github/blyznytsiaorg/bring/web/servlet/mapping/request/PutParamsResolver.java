package io.github.blyznytsiaorg.bring.web.servlet.mapping.request;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.PutMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestMethod;
import io.github.blyznytsiaorg.bring.web.servlet.mapping.RestControllerParams;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The PutParamsResolver class implements the RequestParamsResolver interface
 * to handle the @PutMapping annotation.
 *
 * @see PutMapping
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
@Component
public class PutParamsResolver
        implements RequestParamsResolver {

    /**
     * Retrieves the @PutMapping annotation class.
     *
     * @return The @PutMapping annotation class.
     */
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return PutMapping.class;
    }

    /**
     * Handles the @PutMapping annotation on a method and processes the associated parameters.
     *
     * @param instance                 The instance of the controller.
     * @param method                   The method with the @PutMapping annotation.
     * @param requestMappingPath       The request mapping path.
     * @param restControllerParamsMap  A map containing controller paths and associated parameters.
     */
    @Override
    public void handleAnnotation(Object instance, Method method, String requestMappingPath,
                                 Map<String, List<RestControllerParams>> restControllerParamsMap) {
        PutMapping annotation = method.getAnnotation(PutMapping.class);
        String methodPath = getMethodPath(annotation.path());
        String path = requestMappingPath + methodPath;
        RestControllerParams params = new RestControllerParams(instance, method,
                RequestMethod.PUT, path);
        List<RestControllerParams> getMethodParamsList = Optional.ofNullable(
                        restControllerParamsMap.get(RequestMethod.PUT.name()))
                .orElse(new ArrayList<>());
        addSorted(params, getMethodParamsList);
        restControllerParamsMap.put(RequestMethod.PUT.name(), getMethodParamsList);
        log.debug("Registered PUT method by path: {}", path);
    }
}
