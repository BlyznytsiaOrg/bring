package com.bobocode.bring.web.servlet.mapping;

import com.bobocode.bring.web.servlet.annotation.PathVariable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public interface ParamsResolver {

    Class<? extends Annotation> getAnnotation();

    void handleAnnotation(String requestMappingPath, Method method,
                          Map<String, List<RestControllerParams>> restConrollerParamsMap);

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
