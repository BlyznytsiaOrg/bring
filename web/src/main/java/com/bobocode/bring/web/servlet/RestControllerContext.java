package com.bobocode.bring.web.servlet;

import static com.bobocode.bring.web.utils.RestControllerParamsUtil.getRestControllerParams;

import com.bobocode.bring.core.annotation.Component;
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

/**
 * The {@code RestControllerContext} class represents the context for managing
 * REST controllers and their associated parameters in a web application.
 *<p>
 * This class is responsible for collecting and organizing information about controllers,
 * resolving parameters and performing checks
 *</p>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Component
public class RestControllerContext {

    /**
     * Error message prefix for duplicate paths.
     */
    public static final String ERROR_ON_DUPLICATE_PATH = "Error on duplicate path: ";

    /**
     * Package name for java.lang.
     */
    public static final String JAVA_LANG_PACKAGE = "java.lang";

    /**
     * Exception message for missing @RequestHeader annotation value.
     */
    public static final String REQUEST_HEADER_EXCEPTION_MESSAGE = "Required value for @RequestHeader "
            + "annotation for parameter '%s' of method '%s' is not present";

    /**
     * Exception message for unsupported type in @RequestBody annotation.
     */
    public static final String REQUEST_BODY_EXCEPTION_MESSAGE = "Invalid type '%s' for parameter '%s' annotated "
            + "with @RequestBody of method '%s'";

    /**
     * List of registered BringServlet instances.
     */
    private final List<BringServlet> bringServlets;

    /**
     * List of registered RequestParamsResolver instances.
     */
    private final List<RequestParamsResolver> requestParamsResolvers;

    /**
     * Constructs a new RestControllerContext with the specified BringServlets and RequestParamsResolvers.
     *
     * @param bringServlets           The list of BringServlet instances.
     * @param requestParamsResolvers  The list of RequestParamsResolver instances.
     */
    public RestControllerContext(List<BringServlet> bringServlets,
                                 List<RequestParamsResolver> requestParamsResolvers) {
        this.bringServlets = bringServlets;
        this.requestParamsResolvers = requestParamsResolvers;
    }

    /**
     * Retrieves a map containing controller paths and associated parameters.
     *
     * @return A map containing controller paths and associated parameters.
     * @throws RequestPathDuplicateException If duplicate paths are detected.
     * @throws RequestBodyTypeUnsupportedException If an unsupported type is found in @RequestBody annotation.
     * @throws MissingRequestHeaderAnnotationValueException If a missing @RequestHeader annotation value is detected.
     */
    public Map<String, List<RestControllerParams>> getParamsMap() {
        Map<String, List<RestControllerParams>> restControllerParams = new HashMap<>();
        Map<String, List<String>> methodToPathsMap = new HashMap<>();
        for (var bringServlet : bringServlets) {
            Map<String, List<RestControllerParams>> servletParams
                    = getRestControllerParams(bringServlet, requestParamsResolvers);

            servletParams.forEach((key, value) -> {
                restControllerParams
                        .computeIfAbsent(key, v -> new ArrayList<>())
                        .addAll(value);

                methodToPathsMap
                        .computeIfAbsent(key, k -> new ArrayList<>())
                        .addAll(value.stream().map(RestControllerParams::path).toList());
            });
        }
        checkOnDuplicatePath(methodToPathsMap);
        checkParameters(restControllerParams);
        return restControllerParams;
    }

    /**
     * Checks for duplicate paths and throws an exception if found.
     *
     * @param methodToPathsMap A map containing method names and associated paths.
     * @throws RequestPathDuplicateException If duplicate paths are detected.
     */
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
                .map(entry -> "{ " + entry.getKey() + " " + entry.getValue() + " }")
                .collect(Collectors.joining(", "));

        if (!exceptionMessage.isBlank()) {
            throw new RequestPathDuplicateException(ERROR_ON_DUPLICATE_PATH + exceptionMessage);
        }
    }

    /**
     * Checks parameters for @RequestBody and @RequestHeader annotations.
     *
     * @param params A map containing controller paths and associated parameters.
     * @throws RequestBodyTypeUnsupportedException If an unsupported type is found in @RequestBody annotation.
     * @throws MissingRequestHeaderAnnotationValueException If a missing @RequestHeader annotation value is detected.
     */
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

    /**
     * Checks the presence of a value in the @RequestHeader annotation for a method parameter.
     *
     * @param method         The method containing the parameter.
     * @param parameters     The array of parameters for the method.
     * @param parameterNames The list of parameter names for the method.
     * @param index          The index of the parameter being checked.
     * @throws MissingRequestHeaderAnnotationValueException If the @RequestHeader annotation value is blank.
     */
    private void checkRequestHeaderAnnotation(Method method, Parameter[] parameters,
                                  List<String> parameterNames, int index) {
        RequestHeader annotation = parameters[index].getAnnotation(RequestHeader.class);
        String requestHeaderValue = annotation.value();
        if (requestHeaderValue.isBlank()) {
            String parameterName = parameterNames.get(index);
            throw new MissingRequestHeaderAnnotationValueException(
                    String.format(REQUEST_HEADER_EXCEPTION_MESSAGE,
                            parameterName, method.getName()));
        }
    }

    /**
     * Checks the type of parameter annotated with @RequestBody and throws an exception if unsupported.
     *
     * @param method         The method containing the parameter.
     * @param parameters     The array of parameters for the method.
     * @param parameterNames The list of parameter names for the method.
     * @param index          The index of the parameter being checked.
     * @throws RequestBodyTypeUnsupportedException If the type annotated with @RequestBody is unsupported.
     */
    private void checkRequestBodyAnnotation(Method method, Parameter[] parameters,
                                                   List<String> parameterNames, int index) {
        Class<?> type = parameters[index].getType();
        String packageName = type.getPackageName();
        if (!type.equals(String.class) && !type.equals(byte[].class)
                && packageName.contains(JAVA_LANG_PACKAGE)) {
            String parameterName = parameterNames.get(index);
            throw new RequestBodyTypeUnsupportedException(
                    String.format(REQUEST_BODY_EXCEPTION_MESSAGE,
                            type.getSimpleName(), parameterName, method.getName()));
        }
    }
}
