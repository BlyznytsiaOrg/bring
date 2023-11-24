package com.bobocode.bring.web.servlet.mapping.request;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.servlet.annotation.GetMapping;
import com.bobocode.bring.web.servlet.annotation.RequestMethod;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class GetParamsResolver
        implements RequestParamsResolver {
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GetMapping.class;
    }

    @Override
    public void handleAnnotation(Object instance, Method method, String requestMappingPath,
                                 Map<String, List<RestControllerParams>> restConrollerParamsMap) {
        GetMapping annotation = method.getAnnotation(GetMapping.class);
        String methodPath = getMethodPath(annotation.path());
        String path = requestMappingPath + methodPath;
        RestControllerParams params = new RestControllerParams(instance, method,
                RequestMethod.GET, path);
        List<RestControllerParams> getMethodParamsList = Optional.ofNullable(
                restConrollerParamsMap.get(RequestMethod.GET.name()))
                .orElse(new ArrayList<>());
        addSorted(params, getMethodParamsList);
        restConrollerParamsMap.put(RequestMethod.GET.name(), getMethodParamsList);
    }
}
