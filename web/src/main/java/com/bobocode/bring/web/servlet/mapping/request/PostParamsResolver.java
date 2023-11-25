package com.bobocode.bring.web.servlet.mapping.request;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.servlet.annotation.PostMapping;
import com.bobocode.bring.web.servlet.annotation.RequestMethod;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PostParamsResolver
        implements RequestParamsResolver {
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return PostMapping.class;
    }

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
    }
}
