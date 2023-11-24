package com.bobocode.bring.web.utils;

import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.annotation.RequestMapping;
import com.bobocode.bring.web.servlet.mapping.request.RequestParamsResolver;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import lombok.experimental.UtilityClass;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public final class RestControllerParamsUtil {

    public static Map<String, List<RestControllerParams>> getRestControllerParams(BringServlet bringServlet,
                                                                                  List<RequestParamsResolver> requestParamsResolvers) {
        Class<? extends BringServlet> clazz = bringServlet.getClass();
        Map<String, List<RestControllerParams>> restConrollerParamsMap = new HashMap<>();
        String requestMappingPath;
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            requestMappingPath = clazz.getAnnotation(RequestMapping.class).path();
        } else {
            requestMappingPath = "";
        }
        for (Method method : clazz.getMethods()) {
                requestParamsResolvers.stream()
                        .filter(resolver -> method.isAnnotationPresent(resolver.getAnnotation()))
                        .findFirst()
                        .ifPresent(resolver -> resolver.handleAnnotation(bringServlet, method, requestMappingPath,
                                 restConrollerParamsMap));
            }
        return restConrollerParamsMap;
    }
}

