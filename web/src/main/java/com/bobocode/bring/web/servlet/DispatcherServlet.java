package com.bobocode.bring.web.servlet;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.annotation.*;
import com.bobocode.bring.web.exception.MethodArgumentTypeMismatchException;
import com.bobocode.bring.web.exception.TypeArgumentUnsupportedException;
import com.bobocode.bring.web.utils.ReflectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class DispatcherServlet extends FrameworkServlet {
    public static final String BRING_CONTEXT = "BRING_CONTEXT";
    private final List<BringServlet> bringServlets;

    public DispatcherServlet(List<BringServlet> bringServlets) {
        this.bringServlets = bringServlets;
    }

    //    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        ThreadLocal<String> id = new ThreadLocal<>();
//        id.set(UUID.randomUUID().toString());
//        req.setAttribute("id", id);
//        System.out.println("I am in doGet of DispatcherServlet");
//        var bringContext = (BringApplicationContext) req.getServletContext().getAttribute(BRING_CONTEXT);

//        Map<String, Object> allBeans = bringContext.getAllBeans();
//        StringBuilder mapAsString = new StringBuilder();
//        allBeans.keySet().stream().map(key -> key + "=" + allBeans.get(key) + ", ").forEach(mapAsString::append);
//
//        resp.addHeader("Trace-Id", id.get());
//
//        var writer = resp.getWriter();
//        writer.println(mapAsString);
//        writer.flush();
//    }

    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        bringServlets.stream()
                .map(bringServlet -> processRestController(bringServlet, req))
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
    public Object processRestController(BringServlet bringServlet, HttpServletRequest req) {
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
                    if (checkIfPathVariableAnnotationIsPresent(parameters)) {
                        int index = requestPath.lastIndexOf("/");
                        String requestPathShortened = requestPath.substring(0, index + 1);
                        if (requestPathShortened.equals(params.path())){
                            Object[] args = prepareArgs(req, requestPath, method);
                            return method.invoke(bringServlet, args);
                        }
                    } else if (checkIfRequestParamAnnotationIsPresent(parameters)) {
                        if (requestPath.equals(params.path())) {
                            Object[] args = prepareArgs(req, requestPath, method);
                            return method.invoke(bringServlet, args);
                        }
                    }
                }
            }
        }
        return String.format("This application has no explicit mapping for '%s'", requestPath);
    }

    private boolean checkIfRequestParamAnnotationIsPresent(Parameter[] parameters) {
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfPathVariableAnnotationIsPresent(Parameter[] parameters) {
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(PathVariable.class)) {
                return true;
            }
        }
        return false;
    }

    private Object[] prepareArgs(HttpServletRequest req, String requestPath,
                                 Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
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
                }
            }
        }
        return args;
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
                if (method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping annotation = method.getAnnotation(GetMapping.class);
                    String methodPath = getMethodPath(annotation.path());
                    String path = requestMappingPath + methodPath;
                    RestControllerParams restControllerParams = getRestControllerParams(method, path, RequestMethod.GET);
                    List<RestControllerParams> getMethodParamsList = Optional.ofNullable(restConrollerParamsMap.get(RequestMethod.GET.name()))
                            .orElse(new ArrayList<>());
                    addSorted(restControllerParams, getMethodParamsList);
                    restConrollerParamsMap.put(RequestMethod.GET.name(), getMethodParamsList);
                } else if (method.isAnnotationPresent(PostMapping.class)) {
                    PostMapping annotation = method.getAnnotation(PostMapping.class);
                    String methodPath = annotation.path();
                    String path = requestMappingPath + methodPath;
                    RestControllerParams restControllerParams = getRestControllerParams(method, path, RequestMethod.POST);
                    List<RestControllerParams> getMethodParamsList = Optional.ofNullable(restConrollerParamsMap.get(RequestMethod.POST.name()))
                            .orElse(new ArrayList<>());
                    addSorted(restControllerParams, getMethodParamsList);
                    restConrollerParamsMap.put(RequestMethod.POST.name(), getMethodParamsList);
                } else if (method.isAnnotationPresent(PutMapping.class)) {
                    PutMapping annotation = method.getAnnotation(PutMapping.class);
                    String methodPath = annotation.path();
                    String path = requestMappingPath + methodPath;
                    RestControllerParams restControllerParams = getRestControllerParams(method, path, RequestMethod.PUT);
                    List<RestControllerParams> getMethodParamsList = Optional.ofNullable(restConrollerParamsMap.get(RequestMethod.PUT.name()))
                            .orElse(new ArrayList<>());
                    addSorted(restControllerParams, getMethodParamsList);
                    restConrollerParamsMap.put(RequestMethod.PUT.name(), getMethodParamsList);
                } else if (method.isAnnotationPresent(PatchMapping.class)) {
                    PatchMapping annotation = method.getAnnotation(PatchMapping.class);
                    String methodPath = annotation.path();
                    String path = requestMappingPath + methodPath;
                    RestControllerParams restControllerParams = getRestControllerParams(method, path, RequestMethod.PATCH);
                    List<RestControllerParams> getMethodParamsList = Optional.ofNullable(restConrollerParamsMap.get(RequestMethod.PATCH.name()))
                            .orElse(new ArrayList<>());
                    addSorted(restControllerParams, getMethodParamsList);
                    restConrollerParamsMap.put(RequestMethod.PATCH.name(), getMethodParamsList);
                }
            }
        }
        return restConrollerParamsMap;
    }

    private void addSorted(RestControllerParams restControllerParams,
                           List<RestControllerParams> methodParamsList) {
        Parameter[] parameters = restControllerParams.method.getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(PathVariable.class)) {
                methodParamsList.add(restControllerParams);
                return;
            }
        }
        methodParamsList.add(0, restControllerParams);
    }

    private String getMethodPath(String path) {
        if (path.contains("{") && path.contains("}")) {
            int index = path.lastIndexOf("{");
            path = path.substring(0, index);
        }
        return path;
    }

    private RestControllerParams getRestControllerParams(Method method,
                                                         String path,
                                                         RequestMethod requestMethod) {
        return new RestControllerParams(method, requestMethod, path);

    }

    public String getRequestPath(HttpServletRequest req) {
        String contextPath = req.getContextPath();
        String requestURI = req.getRequestURI();
        if (requestURI.startsWith(contextPath)) {
            requestURI = requestURI.replace(contextPath, "");
        }
        return requestURI;
    }

    public record RestControllerParams(Method method, RequestMethod requestMethod, String path) {
    }
}
