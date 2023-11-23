package com.bobocode.bring.web.servlet;

import static com.bobocode.bring.web.utils.RestControllerParamsUtil.getRestControllerParams;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.servlet.exception.RequestPathDuplicateException;
import com.bobocode.bring.web.servlet.mapping.request.RequestParamsResolver;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RestControllerContext {

    public static final String ERROR_ON_DUPLICATE_PATH = "Error on duplicate path: ";
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
                List<RestControllerParams> params = Optional.ofNullable(restControllerParams.get(key))
                        .orElseGet(ArrayList::new);
                params.addAll(value);
                restControllerParams.put(key, params);
            });

            servletParams.forEach((key, value) -> {
                List<String> existingParamsList = Optional.ofNullable(methodToPathsMap.get(key))
                        .orElseGet(ArrayList::new);
                List<String> paths = value.stream()
                        .map(RestControllerParams::path)
                        .toList();
                existingParamsList.addAll(paths);
                methodToPathsMap.put(key, existingParamsList);
            });
        }
        checkOnDuplicatePath(methodToPathsMap);
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
}
