package com.bobocode.bring.web.servlet;

import static com.bobocode.bring.web.utils.HttpServletRequestUtils.getRequestPath;
import static com.bobocode.bring.web.utils.HttpServletRequestUtils.getShortenedPath;
import static com.bobocode.bring.web.utils.ParameterTypeUtils.parseToParameterType;

import com.bobocode.bring.core.anotation.Component;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The {@code DispatcherServlet} class extends {@code FrameworkServlet} and serves as the central
 * dispatcher for handling HTTP requests in a RESTful web application.
 * It processes incoming requests, resolves appropriate controllers and manages response generation.
 *
 *<p>
 * The class supports the annotation-based mapping of controllers and provides a flexible mechanism
 * for handling various types of parameters in controller methods.
 *</p>
 *
 * <p>
 * Key Constants:
 * - {@code REST_CONTROLLER_PARAMS}: Key for storing REST controller parameters in the servlet context.
 * - {@code REGEX_STATIC_URL}: Regular expression for matching static URLs.
 *</p>
 *
 * <p>
 * Key Components:
 * - {@code responseAnnotationResolver}: List of response annotation resolvers for handling
 *   custom annotations on controller methods.
 * - {@code objectMapper}: Object mapper for converting between JSON and Java objects.
 *</p>
 *
 * @see ResponseAnnotationResolver
 * @see RestControllerContext
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Component
@Slf4j
public class DispatcherServlet extends FrameworkServlet {
    public static final String REST_CONTROLLER_PARAMS = "REST_CONTROLLER_PARAMS";
    public static final String REGEX_STATIC_URL = "^/static/.*$";
    private final List<ResponseAnnotationResolver> responseAnnotationResolver;
    private final ObjectMapper objectMapper;

    public DispatcherServlet(List<ResponseAnnotationResolver> responseAnnotationResolver,
                             ObjectMapper objectMapper) {
        this.responseAnnotationResolver = responseAnnotationResolver;
        this.objectMapper = objectMapper;
    }

    /**
     * Overrides the {@code processRequest} method of {@code FrameworkServlet}.
     * Processes incoming HTTP requests by obtaining REST controller parameters and
     * delegating the processing to {@code processRestControllerRequest}.
     *
     * @param req  HttpServletRequest object representing the HTTP request.
     * @param resp HttpServletResponse object representing the HTTP response.
     */
    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        RestControllerParams restControllerParams = getRestControllerParams(req);
        processRestControllerRequest(restControllerParams, req, resp);
    }

    /**
     * Retrieves the {@code RestControllerParams} for the given {@code HttpServletRequest}.
     * The method uses the request's method and path to find the corresponding controller parameters
     * stored in the servlet context. It filters the parameters based on the path and path variables,
     * then returns the first match. If no match is found, it throws a {@code MissingApplicationMappingException}.
     *
     * @param req HttpServletRequest object representing the HTTP request.
     * @return The matched {@code RestControllerParams} for the request.
     * @throws MissingApplicationMappingException If no explicit mapping is found for the request.
     */
    @SuppressWarnings("unchecked")
    private RestControllerParams getRestControllerParams(HttpServletRequest req) {
        String requestPath = getRequestPath(req);
        String methodName = req.getMethod();
        var restControllerParams = (Map<String, List<RestControllerParams>>) req.getServletContext()
                .getAttribute(REST_CONTROLLER_PARAMS);

        return restControllerParams.getOrDefault(methodName, Collections.emptyList())
                .stream()
                .filter(params -> checkParams(requestPath, params))
                .findFirst()
                .orElseThrow(() -> new MissingApplicationMappingException(
                        String.format("This application has no explicit mapping for '%s'", getRequestPath(req))));
    }

    /**
     * Checks if the given {@code RestControllerParams} match the provided request path.
     * If the controller method has path variables, it shortens the request path accordingly.
     * The method returns true if the paths match, considering path variables and static URLs.
     *
     * @param requestPath The path of the HTTP request.
     * @param params      The {@code RestControllerParams} to check against.
     * @return True if the paths match; false otherwise.
     */
    private boolean checkParams(String requestPath, RestControllerParams params) {
        Method method = params.method();
        Parameter[] parameters = method.getParameters();
        if (checkIfPathVariableAnnotationIsPresent(parameters)) {
            String requestPathShortened = getShortenedPath(requestPath);
            return requestPathShortened.equals(params.path());
        }
        return requestPath.equals(params.path()) || checkIfUrlIsStatic(requestPath, params.path());
    }

    /**
     * Processes the HTTP request for a specific {@code RestControllerParams}.
     * Invokes the corresponding controller method with the provided arguments and handles the resulting response.
     *
     * @param params The {@code RestControllerParams} representing the controller method to invoke.
     * @param req    HttpServletRequest object representing the HTTP request.
     * @param resp   HttpServletResponse object representing the HTTP response.
     */
    @SneakyThrows
    private void processRestControllerRequest(RestControllerParams params,
                                              HttpServletRequest req,
                                              HttpServletResponse resp) {
        String requestPath = getRequestPath(req);
        Object instance = params.instance();
        Method method = params.method();
        Object[] args = (method.getParameterCount() == 0)
                ? new Object[0]
                : prepareArgs(req, resp, requestPath, method);

        getRestControllerProcessResult(instance, method, resp, args);
    }

    /**
     * Invokes the controller method with the provided arguments and handles the resulting response.
     * If the method returns a non-null result, it is passed to the {@code performResponse} method
     * along with method metadata (e.g., annotations). The response is then written to the provided {@code HttpServletResponse}.
     *
     * @param instance The instance of the controller.
     * @param method   The controller method to invoke.
     * @param resp     The {@code HttpServletResponse} object representing the HTTP response.
     * @param args     The arguments to be passed to the controller method.
     * @throws IllegalAccessException    If the method is inaccessible.
     * @throws InvocationTargetException If the invoked method throws an exception.
     */
    private void getRestControllerProcessResult(
            Object instance, Method method, HttpServletResponse resp, Object... args)
            throws IllegalAccessException, InvocationTargetException {
        Optional.ofNullable(method.invoke(instance, args))
                .ifPresent(result -> performResponse(new RestControllerProcessResult(method, result), resp));
    }

    /**
     * Handles the response generated by a controller method.
     * Checks for custom response annotations using registered {@code ResponseAnnotationResolver}s,
     * applies the annotations if found, and then writes the response to the provided {@code HttpServletResponse}.
     *
     * @param processResult The result of the controller method execution, including metadata.
     * @param resp          The {@code HttpServletResponse} object representing the HTTP response.
     */
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

    /**
     * Checks if the provided request path and parameter path match a static URL pattern.
     * Static URLs are defined by a regular expression specified by {@code REGEX_STATIC_URL}.
     *
     * @param requestPath The path from the HTTP request.
     * @param paramPath   The path from the controller method parameter.
     * @return {@code true} if both paths match the static URL pattern; otherwise, {@code false}.
     */
    private boolean checkIfUrlIsStatic(String requestPath, String paramPath) {
        Pattern patternStaticUrl = Pattern.compile(REGEX_STATIC_URL);
        return patternStaticUrl.matcher(requestPath).matches() && patternStaticUrl.matcher(paramPath).matches();
    }

    /**
     * Checks if any of the given parameters are annotated with {@code PathVariable}.
     *
     * @param parameters The array of parameters to check.
     * @return {@code true} if at least one parameter is annotated with {@code PathVariable}; otherwise, {@code false}.
     */
    private boolean checkIfPathVariableAnnotationIsPresent(Parameter[] parameters) {
        return Arrays.stream(parameters)
                .anyMatch(parameter -> parameter.isAnnotationPresent(PathVariable.class));
    }

    /**
     * Prepares the arguments for invoking a controller method by populating an array with values
     * obtained from the given {@code HttpServletRequest}, {@code HttpServletResponse}, and request path.
     *
     * @param req         The {@code HttpServletRequest} object representing the HTTP request.
     * @param resp        The {@code HttpServletResponse} object representing the HTTP response.
     * @param requestPath The path from the HTTP request.
     * @param method      The controller method for which arguments are being prepared.
     * @return An array of objects representing the prepared arguments for the controller method.
     */
    @SneakyThrows
    private Object[] prepareArgs(HttpServletRequest req, HttpServletResponse resp,
                                 String requestPath, Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            getParameterValue(req, resp, requestPath, method, parameters, args, i);
        }
        return args;
    }

    /**
     * Extracts the value for the method parameter at index {@code index} based on its annotations and type.
     * The extracted value is then set in the {@code args} array, which represents the prepared arguments
     * for invoking a controller method.
     *
     * @param req         The {@code HttpServletRequest} object representing the HTTP request.
     * @param resp        The {@code HttpServletResponse} object representing the HTTP response.
     * @param requestPath The path from the HTTP request.
     * @param method      The controller method for which arguments are being prepared.
     * @param parameters  The array of parameters of the controller method.
     * @param args        The array of objects representing the prepared arguments.
     * @param index       The index of the current parameter being processed.
     * @throws IOException If an I/O error occurs while processing the request or response.
     * @throws MissingRequestParamException If a required request parameter is not present.
     */
    private void getParameterValue(HttpServletRequest req, HttpServletResponse resp, String requestPath,
                           Method method, Parameter[] parameters, Object[] args, int index)
            throws IOException {
        if (parameters[index].isAnnotationPresent(PathVariable.class)) {
            extractPathVariable(requestPath, args, parameters, index);
        } else if (parameters[index].isAnnotationPresent(RequestParam.class)) {
            extractRequestParam(req, method, args, index, parameters);
        } else if (parameters[index].isAnnotationPresent(RequestBody.class)) {
            extractRequestBody(req, args, parameters, index);
        } else if (parameters[index].isAnnotationPresent(RequestHeader.class)) {
            extractRequestHeaderParam(req, args, parameters, index);
        } else if (parameters[index].getType().equals(HttpServletRequest.class)) {
            args[index] = req;
        } else if (parameters[index].getType().equals(HttpServletResponse.class)) {
            args[index] = resp;
        }
    }

    /**
     * Extracts the value of a request header parameter based on the specified annotation and sets
     * it in the corresponding position of the {@code args} array.
     *
     * @param req         The {@code HttpServletRequest} object representing the HTTP request.
     * @param args        The array of objects representing the prepared arguments.
     * @param parameters  The array of parameters of the controller method.
     * @param index           The index of the current parameter being processed.
     */
    private void extractRequestHeaderParam(HttpServletRequest req, Object[] args,
                                           Parameter[] parameters, int index) {
        RequestHeader annotation = parameters[index].getAnnotation(RequestHeader.class);
        String value = annotation.value();
        String header = req.getHeader(value);
        args[index] = header;
    }

    /**
     * Extracts the value of a request body parameter based on the specified annotation and sets
     * it in the corresponding position of the {@code args} array.
     *
     * @param req         The {@code HttpServletRequest} object representing the HTTP request.
     * @param args        The array of objects representing the prepared arguments.
     * @param parameters  The array of parameters of the controller method.
     * @param index       The index of the current parameter being processed.
     * @throws IOException If an I/O error occurs while processing the request body.
     */
    private void extractRequestBody(HttpServletRequest req, Object[] args,
                                    Parameter[] parameters, int index)
            throws IOException {
        Class<?> type = parameters[index].getType();
        BufferedReader reader = req.getReader();
        if (type.equals(String.class)) {
            args[index] = reader.lines().collect(Collectors.joining());
        } else if (type.equals(Map.class)) {
            args[index] = objectMapper.readValue(reader, Map.class);
        } else if (type.equals(byte[].class)) {
            args[index] = objectMapper.readValue(req.getInputStream(), byte[].class);
        } else {
            args[index] = objectMapper.readValue(reader, type);
        }
    }

    /**
     * Extracts the value of a request parameter based on the specified annotation and sets
     * it in the corresponding position of the {@code args} array.
     *
     * @param req         The {@code HttpServletRequest} object representing the HTTP request.
     * @param method      The controller method for which arguments are being prepared.
     * @param args        The array of objects representing the prepared arguments.
     * @param index       The index of the current parameter being processed.
     * @param parameters  The array of parameters of the controller method.
     * @throws MissingRequestParamException If a required request parameter is not present.
     */
    private void extractRequestParam(HttpServletRequest req, Method method, Object[] args,
                                            int index, Parameter[] parameters) {
        List<String> parameterNames = ReflectionUtils.getParameterNames(method);
        String parameterName = parameterNames.get(index);
        Class<?> type = parameters[index].getType();
        String parameterValue = req.getParameter(parameterName);
        if (parameterValue != null) {
            args[index] = parseToParameterType(parameterValue, type);
        } else {
            throw new MissingRequestParamException(
                    String.format("Required request parameter '%s' "
                                  + "for method parameter type '%s' is not present",
                            parameterName, type.getSimpleName()));
        }
    }

    /**
     * Extracts the value of a path variable based on the specified annotation and sets
     * it in the corresponding position of the {@code args} array.
     *
     * @param requestPath The path from the HTTP request.
     * @param args        The array of objects representing the prepared arguments.
     * @param parameters  The array of parameters of the controller method.
     * @param index       The index of the current parameter being processed.
     */
    private void extractPathVariable(String requestPath, Object[] args,
                                            Parameter[] parameters, int index) {
        int lastIndexOf = requestPath.lastIndexOf("/");
        String pathVariable = requestPath.substring(lastIndexOf + 1);
        Class<?> type = parameters[index].getType();
        args[index] = parseToParameterType(pathVariable, type);
    }
}
