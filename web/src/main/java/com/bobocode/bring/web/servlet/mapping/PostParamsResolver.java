package com.bobocode.bring.web.servlet.mapping;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.servlet.annotation.PostMapping;
import com.bobocode.bring.web.servlet.annotation.RequestMethod;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PostParamsResolver
        implements ParamsResolver {
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return PostMapping.class;
    }

    @Override
    public void handleAnnotation(String requestMappingPath, Method method,
                                 Map<String, List<RestControllerParams>> restConrollerParamsMap) {
        PostMapping annotation = method.getAnnotation(PostMapping.class);
        String methodPath = getMethodPath(annotation.path());
        String path = requestMappingPath + methodPath;
        RestControllerParams params = new RestControllerParams(method, RequestMethod.POST, path);
        List<RestControllerParams> getMethodParamsList = Optional.ofNullable(
                restConrollerParamsMap.get(RequestMethod.POST.name()))
                .orElse(new ArrayList<>());
        addSorted(params, getMethodParamsList);
        restConrollerParamsMap.put(RequestMethod.POST.name(), getMethodParamsList);
    }
}
