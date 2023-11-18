package com.bobocode.bring.web.servlet.mapping;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.annotation.PutMapping;
import com.bobocode.bring.web.annotation.RequestMethod;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PutParamsResolver
        implements ParamsResolver {
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return PutMapping.class;
    }

    @Override
    public void handleAnnotation(String requestMappingPath, Method method,
                                 Map<String, List<RestControllerParams>> restConrollerParamsMap) {
        PutMapping annotation = method.getAnnotation(PutMapping.class);
        String methodPath = getMethodPath(annotation.path());
        String path = requestMappingPath + methodPath;
        RestControllerParams params = new RestControllerParams(method, RequestMethod.PUT, path);
        List<RestControllerParams> getMethodParamsList = Optional.ofNullable(
                        restConrollerParamsMap.get(RequestMethod.PUT.name()))
                .orElse(new ArrayList<>());
        addSorted(params, getMethodParamsList);
        restConrollerParamsMap.put(RequestMethod.PUT.name(), getMethodParamsList);
    }
}
