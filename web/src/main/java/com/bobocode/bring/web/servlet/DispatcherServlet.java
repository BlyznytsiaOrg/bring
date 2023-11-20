package com.bobocode.bring.web.servlet;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.servlet.annotation.PathVariable;
import com.bobocode.bring.web.servlet.annotation.RequestBody;
import com.bobocode.bring.web.servlet.annotation.RequestMapping;
import com.bobocode.bring.web.servlet.annotation.RequestParam;
import com.bobocode.bring.web.servlet.exception.MissingServletRequestParameterException;
import com.bobocode.bring.web.servlet.mapping.ParamsResolver;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bobocode.bring.web.utils.HttpServletRequestUtils.getRequestPath;
import static com.bobocode.bring.web.utils.ParameterTypeUtils.parseToParameterType;

@Component
@Slf4j
public class DispatcherServlet extends FrameworkServlet {
    private final List<BringServlet> bringServlets;
    private final List<ParamsResolver> paramsResolvers;
    private final ObjectMapper objectMapper;

    public DispatcherServlet(List<BringServlet> bringServlets,
                             List<ParamsResolver> paramsResolvers,
                             ObjectMapper objectMapper) {
        this.bringServlets = bringServlets;
        this.paramsResolvers = paramsResolvers;
        this.objectMapper = objectMapper;
    }

    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) {

        bringServlets.stream()
                .map(bringServlet -> processRestController(bringServlet, req, resp))
                .findFirst()
                .ifPresent(response -> performResponse(response, resp));
    }

    @SneakyThrows
    public void performResponse(Object response, HttpServletResponse resp) {
        try (PrintWriter writer = resp.getWriter()){
            writer.print(response);
            writer.flush();
        }
    }

    @SneakyThrows
    public Object processRestController(BringServlet bringServlet, HttpServletRequest req,
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
                    return method.invoke(bringServlet);
                } else {
                    if (checkIfPathVariableAnnotationIsPresent(parameters)) {
                        int index = requestPath.lastIndexOf("/");
                        String requestPathShortened = requestPath.substring(0, index + 1);
                        if (requestPathShortened.equals(params.path())) {
                            return invokeMethodWithArgs(bringServlet, req, resp, requestPath, method);
                        }
                    } else if (requestPath.equals(params.path())) {
                        return invokeMethodWithArgs(bringServlet, req, resp, requestPath, method);
                    }
                }
            }
        }
        return String.format("This application has no explicit mapping for '%s'", requestPath);
    }

    private boolean checkIfPathVariableAnnotationIsPresent(Parameter[] parameters) {
        return Arrays.stream(parameters).anyMatch(parameter -> parameter.isAnnotationPresent(PathVariable.class));
    }

    private Object invokeMethodWithArgs(BringServlet bringServlet, HttpServletRequest req, HttpServletResponse resp,
                                        String requestPath, Method method)
            throws InvocationTargetException, IllegalAccessException {
        Object[] args = prepareArgs(req, resp, requestPath, method);
        return method.invoke(bringServlet, args);
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
            } else if (parameters[i].getType().equals(HttpServletRequest.class)) {
                args[i] = req;
            } else if (parameters[i].getType().equals(HttpServletResponse.class)) {
                args[i] = resp;
            }
        }

        return args;
    }

    private void extractRequestBody(HttpServletRequest req, Object[] args, Parameter[] parameters, int i)
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

    private static void extractPathVariable(String requestPath, Object[] args, Parameter[] parameters, int i) {
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
