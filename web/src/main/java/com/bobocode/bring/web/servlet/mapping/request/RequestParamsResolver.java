package com.bobocode.bring.web.servlet.mapping.request;

import com.bobocode.bring.web.servlet.annotation.PathVariable;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public interface RequestParamsResolver {

    Class<? extends Annotation> getAnnotation();

    void handleAnnotation(Object instance, Method method, String requestMappingPath,
                          Map<String, List<RestControllerParams>> restControllerParamsMap);

    default String getMethodPath(String path) {
        if (path.contains("{") && path.contains("}")) {
            int index = path.lastIndexOf("{");
            path = path.substring(0, index);
        }
        return path;
    }

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
