package com.bobocode.bring.web.servlet;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.server.properties.ServerProperties;
import com.bobocode.bring.web.servlet.annotation.*;
import com.bobocode.bring.web.servlet.exception.MissingApplicationMappingException;
import com.bobocode.bring.web.servlet.exception.MissingRequestHeaderAnnotationValueException;
import com.bobocode.bring.web.servlet.exception.MissingServletRequestParameterException;
import com.bobocode.bring.web.servlet.mapping.ParamsResolver;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import com.bobocode.bring.web.servlet.mapping.RestControllerProcessResult;
import com.bobocode.bring.web.utils.ReflectionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.bobocode.bring.web.utils.HttpServletRequestUtils.getRequestPath;
import static com.bobocode.bring.web.utils.ParameterTypeUtils.parseToParameterType;

@Component
@Slf4j
public class DispatcherServlet extends FrameworkServlet {
    private final List<BringServlet> bringServlets;
    private final List<ParamsResolver> paramsResolvers;
    private final ObjectMapper objectMapper;
    private final ServerProperties serverProperties;

    public DispatcherServlet(List<BringServlet> bringServlets,
                             List<ParamsResolver> paramsResolvers,
                             ObjectMapper objectMapper,
                             ServerProperties serverProperties) {
        this.bringServlets = bringServlets;
        this.paramsResolvers = paramsResolvers;
        this.objectMapper = objectMapper;
        this.serverProperties = serverProperties;
    }

    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        BringServlet servlet = bringServlets.stream()
                .filter(bringServlet -> controllerIsPresent(bringServlet, req, resp))
                .findFirst()
                .orElseThrow(() -> new MissingApplicationMappingException(
                        String.format("This application has no explicit mapping for '%s'", getRequestPath(req))));

        processRestControllerRequest(servlet, req, resp);
    }

    @SneakyThrows
    public void performResponse(RestControllerProcessResult result, HttpServletResponse resp) {
        Method method = result.method();
        if (method.isAnnotationPresent(ResponseStatus.class)) {
            ResponseStatus annotation = method.getAnnotation(ResponseStatus.class);
            int statusValue = annotation.value().getValue();
            resp.setStatus(statusValue);
        }
        try (PrintWriter writer = resp.getWriter()) {
            Object response = result.result();
            writer.print(response);
            writer.flush();
        }
    }

    @SneakyThrows
    public void processRestControllerRequest(BringServlet bringServlet,
                                             HttpServletRequest req,
                                             HttpServletResponse resp) {
        String requestPath = getRequestPath(req);
        String methodName = req.getMethod();
        var restControllerParamsMap = getRestControllerParams(bringServlet.getClass());
        var methodParamsList = restControllerParamsMap.get(methodName);
        if (methodParamsList != null) {
            for (RestControllerParams params : methodParamsList) {
                Method method = params.method();
                Parameter[] parameters = method.getParameters();
                if (parameters.length == 0 && requestPath.equals(params.path())) {
                    getRestControllerProcessResult(bringServlet, method, resp);
                } else {
                    String requestPathShortened = "";
                    if (checkIfPathVariableAnnotationIsPresent(parameters)) {
                        requestPathShortened = getShortenedPath(requestPath);
                    }
                    if (requestPathShortened.equals(params.path())
                        || requestPath.equals(params.path())
                        || checkIfUrlIsStatic(requestPath, params.path())) {
                        Object[] args = prepareArgs(req, resp, requestPath, method);
                        getRestControllerProcessResult(bringServlet, method, resp, args);
                    }

                }
            }
        }
    }

    private boolean controllerIsPresent(BringServlet bringServlet, HttpServletRequest req, HttpServletResponse resp) {
        String requestPath = getRequestPath(req);
        String methodName = req.getMethod();
        var restControllerParamsMap = getRestControllerParams(bringServlet.getClass());
        var methodParamsList = restControllerParamsMap.get(methodName);
        if (methodParamsList != null) {
            return methodParamsList.stream()
                    .filter(params -> checkParams(requestPath, params))
                    .findAny()
                    .map(params -> Boolean.TRUE)
                    .orElse(Boolean.FALSE);
        }
        return Boolean.FALSE;
    }

    private boolean checkParams(String requestPath, RestControllerParams params) {
        Method method = params.method();
        Parameter[] parameters = method.getParameters();
        String requestPathShortened = "";
        if (checkIfPathVariableAnnotationIsPresent(parameters)) {
            requestPathShortened = getShortenedPath(requestPath);
        }
        return requestPath.equals(params.path()) || requestPathShortened.equals(params.path())
               || checkIfUrlIsStatic(requestPath, params.path());
    }

    private String getShortenedPath(String requestPath) {
        String requestPathShortened;
        int index = requestPath.lastIndexOf("/");
        requestPathShortened = requestPath.substring(0, index + 1);
        return requestPathShortened;
    }

    private void getRestControllerProcessResult(
            BringServlet bringServlet, Method method, HttpServletResponse resp, Object... args)
            throws IllegalAccessException, InvocationTargetException {
        Optional.ofNullable(method.invoke(bringServlet, args))
                .ifPresent(result -> performResponse(new RestControllerProcessResult(method, result), resp));
    }

    private boolean checkIfUrlIsStatic(String requestPath, String paramPath) {
        Pattern patternStaticUrl = Pattern.compile(serverProperties.getRegexStaticUrl());
        return patternStaticUrl.matcher(requestPath).matches() && patternStaticUrl.matcher(paramPath).matches();
    }

    private boolean checkIfPathVariableAnnotationIsPresent(Parameter[] parameters) {
        return Arrays.stream(parameters)
                .anyMatch(parameter -> parameter.isAnnotationPresent(PathVariable.class));
    }

    @SneakyThrows
    private Object[] prepareArgs(HttpServletRequest req, HttpServletResponse resp,
                                 String requestPath, Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(PathVariable.class)) {
                extractPathVariable(requestPath, args, parameters, i);
            } else if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                extractRequestParam(req, method, args, i, parameters);
            } else if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                extractRequestBody(req, args, parameters, i);
            } else if (parameters[i].isAnnotationPresent(RequestHeader.class)) {
                extractRequestHeaderParam(req, args, parameters, i);
            } else if (parameters[i].getType().equals(HttpServletRequest.class)) {
                args[i] = req;
            } else if (parameters[i].getType().equals(HttpServletResponse.class)) {
                args[i] = resp;
            }
        }
        return args;
    }

    private void extractRequestHeaderParam(HttpServletRequest req, Object[] args,
                                           Parameter[] parameters, int i) {
        RequestHeader annotation = parameters[i].getAnnotation(RequestHeader.class);
        String value = annotation.value();
        if (value == null) {
            throw new MissingRequestHeaderAnnotationValueException(
                    String.format("Required value for @RequestHeader annotation "
                                  + "for parameter '%s' is not present", parameters[i].getName()));
        }
        String header = req.getHeader(value);
        args[i] = header;
    }

    private void extractRequestBody(HttpServletRequest req, Object[] args,
                                    Parameter[] parameters, int i)
            throws IOException {
        Class<?> type = parameters[i].getType();
        if (type.equals(String.class)) {
            args[i] = req.getReader().lines().collect(Collectors.joining());
        } else {
            args[i] = objectMapper.readValue(req.getReader(), type);
        }
    }

    private static void extractRequestParam(HttpServletRequest req, Method method, Object[] args, int i,
                                            Parameter[] parameters) {
        List<String> parameterNames = ReflectionUtils.getParameterNames(method);
        String parameterName = parameterNames.get(i);
        Class<?> type = parameters[i].getType();
        String parameterValue = req.getParameter(parameterName);
        if (parameterValue != null) {
            args[i] = parseToParameterType(parameterValue, type);
        } else {
            throw new MissingServletRequestParameterException(
                    String.format("Required request parameter '%s' "
                                  + "for method parameter type '%s' is not present",
                            parameterName, type.getSimpleName()));
        }
    }

    private static void extractPathVariable(String requestPath, Object[] args,
                                            Parameter[] parameters, int i) {
        int index = requestPath.lastIndexOf("/");
        String pathVariable = requestPath.substring(index + 1);
        Class<?> type = parameters[i].getType();
        args[i] = parseToParameterType(pathVariable, type);
    }

    public Map<String, List<RestControllerParams>> getRestControllerParams(Class<?> clazz) {
        Map<String, List<RestControllerParams>> restConrollerParamsMap = new HashMap<>();
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            String requestMappingPath = clazz.getAnnotation(RequestMapping.class).path();

            for (Method method : clazz.getMethods()) {
                paramsResolvers.stream()
                        .filter(resolver -> method.isAnnotationPresent(resolver.getAnnotation()))
                        .findFirst()
                        .ifPresent(resolver -> resolver.handleAnnotation(requestMappingPath,
                                method, restConrollerParamsMap));
            }
        }
        return restConrollerParamsMap;
    }
}
