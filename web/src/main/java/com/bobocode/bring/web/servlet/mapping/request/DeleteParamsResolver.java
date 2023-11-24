package com.bobocode.bring.web.servlet.mapping.request;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.servlet.annotation.DeleteMapping;
import com.bobocode.bring.web.servlet.annotation.RequestMethod;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DeleteParamsResolver
        implements RequestParamsResolver {
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return DeleteMapping.class;
    }

    @Override
    public void handleAnnotation(Object instance, Method method, String requestMappingPath,
                                 Map<String, List<RestControllerParams>> restConrollerParamsMap) {
        DeleteMapping annotation = method.getAnnotation(DeleteMapping.class);
        String methodPath = getMethodPath(annotation.path());
        String path = requestMappingPath + methodPath;
        RestControllerParams params = new RestControllerParams(instance, method, RequestMethod.DELETE, path);
        List<RestControllerParams> getMethodParamsList = Optional.ofNullable(
                        restConrollerParamsMap.get(RequestMethod.DELETE.name()))
                .orElse(new ArrayList<>());
        addSorted(params, getMethodParamsList);
        restConrollerParamsMap.put(RequestMethod.DELETE.name(), getMethodParamsList);
    }
}
