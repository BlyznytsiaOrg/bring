package com.bobocode.bring.web.servlet;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.servlet.annotation.PathVariable;
import com.bobocode.bring.web.servlet.annotation.RequestBody;
import com.bobocode.bring.web.servlet.annotation.RequestMapping;
import com.bobocode.bring.web.servlet.annotation.RequestParam;
import com.bobocode.bring.web.servlet.exception.MethodArgumentTypeMismatchException;
import com.bobocode.bring.web.servlet.exception.MissingServletRequestParameterException;
import com.bobocode.bring.web.servlet.exception.TypeArgumentUnsupportedException;
import com.bobocode.bring.web.servlet.mapping.ParamsResolver;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import com.bobocode.bring.web.utils.ReflectionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        PrintWriter writer = resp.getWriter();
        writer.print(response);
        writer.flush();
    }

    @SneakyThrows
    public Object processRestController(BringServlet bringServlet, HttpServletRequest req,
                                        HttpServletResponse resp) {
        String requestPath = getRequestPath(req);
        String methodName = req.getMethod();
        Map<String, List<RestControllerParams>> restControllerParamsMap = getRestControllerParams(bringServlet.getClass());
        List<RestControllerParams> methodParamsList = restControllerParamsMap.get(methodName);
        if (methodParamsList != null) {
            for (RestControllerParams params : methodParamsList) {
                Method method = params.method();
                Parameter[] parameters = method.getParameters();
                if (parameters.length == 0) {
                    if (requestPath.equals(params.path())) {
                        return method.invoke(bringServlet);
                    }
                } else {
                    Object[] args = new Object[parameters.length];
                    if (checkIfPathVariableAnnotationIsPresent(parameters)) {
                        int index = requestPath.lastIndexOf("/");
                        String requestPathShortened = requestPath.substring(0, index + 1);
                        if (requestPathShortened.equals(params.path())) {
                            prepareArgs(req, resp, requestPath, method, args);
                            return method.invoke(bringServlet, args);
                        }
                    } else if (requestPath.equals(params.path())) {
                        prepareArgs(req, resp, requestPath, method, args);
                        return method.invoke(bringServlet, args);
                    }
                }
            }
        }
        return String.format("This application has no explicit mapping for '%s'", requestPath);
    }

    private boolean checkIfPathVariableAnnotationIsPresent(Parameter[] parameters) {
        return Arrays.stream(parameters).anyMatch(parameter -> parameter.isAnnotationPresent(PathVariable.class));
    }

    @SneakyThrows
    private void prepareArgs(HttpServletRequest req, HttpServletResponse resp,
                             String requestPath, Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(PathVariable.class)) {
                int index = requestPath.lastIndexOf("/");
                String pathVariable = requestPath.substring(index + 1);
                Class<?> type = parameters[i].getType();
                args[i] = parseToParameterType(pathVariable, type);
            } else if (parameters[i].isAnnotationPresent(RequestParam.class)) {
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
            } else if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                Class<?> type = parameters[i].getType();
                if (type.equals(String.class)) {
                    args[i] = req.getReader().lines().collect(Collectors.joining());
                } else {
                    args[i] = objectMapper.readValue(req.getReader(), type);
                }
            } else if (parameters[i].getType().equals(HttpServletRequest.class)) {
                args[i] = req;
            } else if (parameters[i].getType().equals(HttpServletResponse.class)) {
                args[i] = resp;
            }
        }
    }

    private Object parseToParameterType(String pathVariable, Class<?> type) {
        Object obj;
        try {
            if (type.equals(String.class)) {
                obj = pathVariable;
            } else if (type.equals(Long.class) || type.equals(long.class)) {
                obj = Long.parseLong(pathVariable);
            } else if (type.equals(Double.class) || type.equals(double.class)) {
                obj = Double.parseDouble(pathVariable);
            } else if (type.equals(Float.class) || type.equals(float.class)) {
                obj = Float.parseFloat(pathVariable);
            } else if (type.equals(Integer.class) || type.equals(int.class)) {
                obj = Integer.parseInt(pathVariable);
            } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                obj = Byte.parseByte(pathVariable);
            } else if (type.equals(Short.class) || type.equals(short.class)) {
                obj = Short.parseShort(pathVariable);
            } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                if (pathVariable.equals("true")) {
                    obj = Boolean.TRUE;
                } else if (pathVariable.equals("false")) {
                    obj = Boolean.FALSE;
                } else {
                    throw new MethodArgumentTypeMismatchException(
                            String.format("Failed to convert value of type 'java.lang.String' "
                                    + "to required type '%s'; Invalid value [%s]", type.getName(), pathVariable));
                }
            } else {
                throw new TypeArgumentUnsupportedException(
                        String.format("The type parameter: '%s' is not supported", type.getName()));
            }
            return obj;
        } catch (NumberFormatException exception) {
            throw new MethodArgumentTypeMismatchException(
                    String.format("Failed to convert value of type 'java.lang.String' "
                            + "to required type '%s'; Invalid value [%s]", type.getName(), pathVariable));
        }
    }

    public Map<String, List<RestControllerParams>> getRestControllerParams(Class<?> clazz) {
        Map<String, List<RestControllerParams>> restConrollerParamsMap = new HashMap<>();
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMappingAnnotation = clazz.getAnnotation(RequestMapping.class);
            String requestMappingPath = requestMappingAnnotation.path();

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

    public String getRequestPath(HttpServletRequest req) {
        String contextPath = req.getContextPath();
        String requestURI = req.getRequestURI();
        if (requestURI.startsWith(contextPath)) {
            requestURI = requestURI.replace(contextPath, "");
        }
        return requestURI;
    }
}
