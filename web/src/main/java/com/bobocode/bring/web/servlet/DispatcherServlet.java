package com.bobocode.bring.web.servlet;

import static com.bobocode.bring.web.utils.HttpServletRequestUtils.getRequestPath;
import static com.bobocode.bring.web.utils.HttpServletRequestUtils.getShortenedPath;
import static com.bobocode.bring.web.utils.ParameterTypeUtils.parseToParameterType;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.web.server.properties.ServerProperties;
import com.bobocode.bring.web.servlet.annotation.PathVariable;
import com.bobocode.bring.web.servlet.annotation.RequestBody;
import com.bobocode.bring.web.servlet.annotation.RequestHeader;
import com.bobocode.bring.web.servlet.annotation.RequestParam;
import com.bobocode.bring.web.servlet.exception.MissingApplicationMappingException;
import com.bobocode.bring.web.servlet.exception.MissingRequestParamException;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import com.bobocode.bring.web.servlet.mapping.RestControllerProcessResult;
import com.bobocode.bring.web.servlet.mapping.response.ResponseAnnotationResolver;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DispatcherServlet extends FrameworkServlet {
    public static final String REST_CONTROLLER_PARAMS = "REST_CONTROLLER_PARAMS";
    private final List<ResponseAnnotationResolver> responseAnnotationResolver;
    private final ObjectMapper objectMapper;
    private final ServerProperties serverProperties;

    public DispatcherServlet(List<ResponseAnnotationResolver> responseAnnotationResolver,
                             ObjectMapper objectMapper,
                             ServerProperties serverProperties) {
        this.responseAnnotationResolver = responseAnnotationResolver;
        this.objectMapper = objectMapper;
        this.serverProperties = serverProperties;
    }

    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        RestControllerParams restControllerParams = getRestControllerParams(req);
        processRestControllerRequest(restControllerParams, req, resp);
    }

    @SuppressWarnings("unchecked")
    private RestControllerParams getRestControllerParams(HttpServletRequest req) {
        String requestPath = getRequestPath(req);
        String methodName = req.getMethod();
        var restControllerParams = (Map<String, List<RestControllerParams>>) req.getServletContext()
                .getAttribute(REST_CONTROLLER_PARAMS);

        var methodParamsList = restControllerParams.get(methodName);
        return Optional.ofNullable(methodParamsList)
                .stream()
                .flatMap(Collection::stream)
                .filter(params -> checkParams(requestPath, params))
                .findAny()
                .orElseThrow(() -> new MissingApplicationMappingException(
                        String.format("This application has no explicit mapping for '%s'", getRequestPath(req))));
    }

    private boolean checkParams(String requestPath, RestControllerParams params) {
        Method method = params.method();
        Parameter[] parameters = method.getParameters();
        if (checkIfPathVariableAnnotationIsPresent(parameters)) {
            String requestPathShortened = getShortenedPath(requestPath);
            return requestPathShortened.equals(params.path());
        }
        return requestPath.equals(params.path()) || checkIfUrlIsStatic(requestPath, params.path());
    }

    @SneakyThrows
    private void processRestControllerRequest(RestControllerParams params,
                                              HttpServletRequest req,
                                              HttpServletResponse resp) {
        String requestPath = getRequestPath(req);
        Object instance = params.instance();
        Method method = params.method();
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) {
            getRestControllerProcessResult(instance, method, resp);
        } else {
            Object[] args = prepareArgs(req, resp, requestPath, method);
            getRestControllerProcessResult(instance, method, resp, args);
        }
    }

    private void getRestControllerProcessResult(
            Object instance, Method method, HttpServletResponse resp, Object... args)
            throws IllegalAccessException, InvocationTargetException {
        Optional.ofNullable(method.invoke(instance, args))
                .ifPresent(result -> performResponse(new RestControllerProcessResult(method, result), resp));
    }

    @SneakyThrows
    private void performResponse(RestControllerProcessResult processResult,
                                 HttpServletResponse resp) {
        Method method = processResult.method();
        HttpServletResponse httpServletResponse = responseAnnotationResolver.stream()
                .filter(resolver -> method.isAnnotationPresent(resolver.getAnnotation()))
                .findFirst()
                .map(resolver -> resolver.handleAnnotation(resp, method))
                .orElse(resp);

        try (PrintWriter writer = httpServletResponse.getWriter()) {
            Object response = processResult.result();
            writer.print(response);
            writer.flush();
        }
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
        String header = req.getHeader(value);
        args[i] = header;
    }

    private void extractRequestBody(HttpServletRequest req, Object[] args,
                                    Parameter[] parameters, int i)
            throws IOException {
        Class<?> type = parameters[i].getType();
        if (type.equals(String.class)) {
            args[i] = req.getReader().lines().collect(Collectors.joining());
        } else if (type.equals(Map.class)) {
            args[i] = objectMapper.readValue(req.getReader(), Map.class);
        } else if (type.equals(byte[].class)) {
            args[i] = objectMapper.readValue(req.getInputStream(), byte[].class);
        } else {
            args[i] = objectMapper.readValue(req.getReader(), type);
        }
    }

    private static void extractRequestParam(HttpServletRequest req, Method method, Object[] args,
                                            int i,
                                            Parameter[] parameters) {
        List<String> parameterNames = ReflectionUtils.getParameterNames(method);
        String parameterName = parameterNames.get(i);
        Class<?> type = parameters[i].getType();
        String parameterValue = req.getParameter(parameterName);
        if (parameterValue != null) {
            args[i] = parseToParameterType(parameterValue, type);
        } else {
            throw new MissingRequestParamException(
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
}
