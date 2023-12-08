package io.github.blyznytsiaorg.bring.web.servlet.mapping.request;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.PostMapping;
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
 * The PostParamsResolver class implements the RequestParamsResolver interface
 * to handle the @PostMapping annotation.
 *
 * @see PostMapping
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
@Component
public class PostParamsResolver
        implements RequestParamsResolver {

    /**
     * Retrieves the @PostMapping annotation class.
     *
     * @return The @PostMapping annotation class.
     */
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return PostMapping.class;
    }

    /**
     * Handles the @PostMapping annotation on a method and processes the associated parameters.
     *
     * @param instance                 The instance of the controller.
     * @param method                   The method with the @PostMapping annotation.
     * @param requestMappingPath       The request mapping path.
     * @param restControllerParamsMap  A map containing controller paths and associated parameters.
     */
    @Override
    public void handleAnnotation(Object instance, Method method, String requestMappingPath,
                                 Map<String, List<RestControllerParams>> restControllerParamsMap) {
        PostMapping annotation = method.getAnnotation(PostMapping.class);
        String methodPath = getMethodPath(annotation.path());
        String path = requestMappingPath + methodPath;
        RestControllerParams params = new RestControllerParams(instance, method,
                RequestMethod.POST, path);
        List<RestControllerParams> getMethodParamsList = Optional.ofNullable(
                restControllerParamsMap.get(RequestMethod.POST.name()))
                .orElse(new ArrayList<>());
        addSorted(params, getMethodParamsList);
        restControllerParamsMap.put(RequestMethod.POST.name(), getMethodParamsList);
        log.debug("Registered POST method by path: {}", path);
    }
}
