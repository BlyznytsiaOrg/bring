package com.bobocode.bring.web.servlet;

import static com.bobocode.bring.web.utils.RestControllerParamsUtil.getRestControllerParams;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.servlet.annotation.RequestBody;
import com.bobocode.bring.web.servlet.annotation.RequestHeader;
import com.bobocode.bring.web.servlet.exception.MissingRequestHeaderAnnotationValueException;
import com.bobocode.bring.web.servlet.exception.RequestBodyTypeUnsupportedException;
import com.bobocode.bring.web.servlet.exception.RequestPathDuplicateException;
import com.bobocode.bring.web.servlet.mapping.request.RequestParamsResolver;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import com.bobocode.bring.web.utils.ReflectionUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RestControllerContext {

    public static final String ERROR_ON_DUPLICATE_PATH = "Error on duplicate path: ";
    public static final String JAVA_LANG_PACKAGE = "java.lang";
    public static final String REQUEST_HEADER_EXCEPTION_MESSAGE = "Required value for @RequestHeader "
            + "annotation for parameter '%s' of method '%s' is not present";
    public static final String REQUEST_BODY_EXCEPTION_MESSAGE = "Invalid type '%s' for parameter '%s' annotated "
            + "with @RequestBody of method '%s'";
    private final List<BringServlet> bringServlets;
    private final List<RequestParamsResolver> requestParamsResolvers;

    public RestControllerContext(List<BringServlet> bringServlets,
                                 List<RequestParamsResolver> requestParamsResolvers) {
        this.bringServlets = bringServlets;
        this.requestParamsResolvers = requestParamsResolvers;
    }

    public Map<String, List<RestControllerParams>> getParamsMap() {
        Map<String, List<RestControllerParams>> restControllerParams = new HashMap<>();
        Map<String, List<String>> methodToPathsMap = new HashMap<>();
        for (var bringServlet : bringServlets) {
            Map<String, List<RestControllerParams>> servletParams
                    = getRestControllerParams(bringServlet, requestParamsResolvers);

            servletParams.forEach((key, value) -> {
                restControllerParams.computeIfAbsent(key, v -> new ArrayList<>()).addAll(value);
                methodToPathsMap.computeIfAbsent(key, k -> new ArrayList<>()).addAll(value.stream()
                        .map(RestControllerParams::path)
                        .toList());
            });
        }
        checkOnDuplicatePath(methodToPathsMap);
        checkParameters(restControllerParams);
        return restControllerParams;
    }

    private void checkOnDuplicatePath(Map<String, List<String>> methodToPathsMap) {
        Map<String, Set<String>> duplicatePaths = new HashMap<>();
        for (var entry : methodToPathsMap.entrySet()) {
            List<String> pathsList = entry.getValue();
            Set<String> duplicateSet = pathsList.stream()
                    .filter(path -> Collections.frequency(pathsList, path) > 1)
                    .collect(Collectors.toSet());
            if(!duplicateSet.isEmpty()) {
                duplicatePaths.put(entry.getKey(), duplicateSet);
            }
        }
        String exceptionMessage = duplicatePaths.entrySet().stream()
                .map(entry -> "{ " + entry.getKey() + " " + entry.getValue().toString() + " }")
                .collect(Collectors.joining(", "));

        if (!exceptionMessage.isBlank()) {
            throw new RequestPathDuplicateException(ERROR_ON_DUPLICATE_PATH + exceptionMessage);
        }
    }

    private void checkParameters(Map<String, List<RestControllerParams>> params) {
        params.forEach((key, value) -> {
            for (var param : value) {
                Method method = param.method();
                Parameter[] parameters = method.getParameters();
                List<String> parameterNames = ReflectionUtils.getParameterNames(method);
                for (int i = 0; i < parameters.length; i++) {
                    if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                        checkRequestBodyAnnotation(method, parameters, parameterNames, i);
                    } else if (parameters[i].isAnnotationPresent(RequestHeader.class)) {
                        checkRequestHeaderAnnotation(method, parameters, parameterNames, i);
                    }
                }
            }
        });
    }

    private void checkRequestHeaderAnnotation(Method method, Parameter[] parameters,
                                  List<String> parameterNames, int i) {
        RequestHeader annotation = parameters[i].getAnnotation(RequestHeader.class);
        String requestHeaderValue = annotation.value();
        if (requestHeaderValue.isBlank()) {
            String parameterName = parameterNames.get(i);
            throw new MissingRequestHeaderAnnotationValueException(
                    String.format(REQUEST_HEADER_EXCEPTION_MESSAGE,
                            parameterName, method.getName()));
        }
    }

    private void checkRequestBodyAnnotation(Method method, Parameter[] parameters,
                                                   List<String> parameterNames, int i) {
        Class<?> type = parameters[i].getType();
        String packageName = type.getPackageName();
        if (!type.equals(String.class) && !type.equals(byte[].class)
                && packageName.contains(JAVA_LANG_PACKAGE)) {
            String parameterName = parameterNames.get(i);
            throw new RequestBodyTypeUnsupportedException(
                    String.format(REQUEST_BODY_EXCEPTION_MESSAGE,
                            type.getSimpleName(), parameterName, method.getName()));
        }
    }
}
