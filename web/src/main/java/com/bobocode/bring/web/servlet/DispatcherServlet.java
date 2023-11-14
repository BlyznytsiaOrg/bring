package com.bobocode.bring.web.servlet;

import com.bobocode.bring.web.annotation.GetMapping;
import com.bobocode.bring.web.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DispatcherServlet extends FrameworkServlet {
    public static final String BRING_CONTEXT = "BRING_CONTEXT";
    List<? extends BringServlet> bringServletList = new ArrayList<>(List.of(new ExampleServlet()));

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
        System.out.println("I am in process request");
        String requestPath = getRequestPath(req);
        bringServletList.stream()
                .map(bringServlet -> processBringServlet(bringServlet, requestPath))
                .findFirst()
                .ifPresent(response -> performResponse(response, resp));
    }

    @SneakyThrows
    public void performResponse(Object response, HttpServletResponse resp) {
        System.out.println(response);
        PrintWriter writer = resp.getWriter();
        writer.println(response);
        writer.flush();
    }

    @SneakyThrows
    public Object processBringServlet(BringServlet bringServlet, String requestPath) {
        ServletParams servletParams = getServletParams(bringServlet.getClass());
        System.out.println(servletParams.path());
        if (requestPath.equals(servletParams.path())) {
            Method method = servletParams.method();
            return method.invoke(bringServlet);
        }
        return new Object();
    }

    public ServletParams getServletParams(Class<?> clazz) {
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
            String requestMappingPath = annotation.path();

            for (Method method : clazz.getMethods()) {
                if (method.getAnnotation(GetMapping.class) != null) {
                    String getMappingPath = method.getAnnotation(GetMapping.class).path();
                   return new ServletParams(method, requestMappingPath + getMappingPath);
                }
            }
        }
        return new ServletParams(null, null);
    }

    public String getRequestPath(HttpServletRequest req) {
        String contextPath = req.getContextPath();
        String requestURI = req.getRequestURI();
        if (requestURI.startsWith(contextPath)) {
            requestURI = requestURI.replace(contextPath, "");
        }
        System.out.println(requestURI);
        return requestURI;
    }

    public record ServletParams(Method method, String path) {
    }
}
