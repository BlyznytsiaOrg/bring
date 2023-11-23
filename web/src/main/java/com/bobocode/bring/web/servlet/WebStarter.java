package com.bobocode.bring.web.servlet;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.server.ServletWebServerFactory;
import com.bobocode.bring.web.server.TomcatServletWebServerFactory;
import com.bobocode.bring.web.server.TomcatWebServer;
import com.bobocode.bring.web.servlet.mapping.RestControllerParams;
import jakarta.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import java.util.List;
import java.util.Map;

@Component
public class WebStarter {
    public static final String BRING_CONTEXT = "BRING_CONTEXT";
    public static final String DISPATCHER_SERVLET_NAME = "dispatcher";
    public static final String URL_PATTERN = "/";
    public static final String REST_CONTROLLER_PARAMS = "REST_CONTROLLER_PARAMS";
    private final ServletWebServerFactory servletWebServerFactory;
    private final DispatcherServlet dispatcherServlet;
    private final JsonExceptionHandler jsonExceptionHandler;
    private final RestControllerContext restControllerContext;
    private TomcatWebServer webServer;


    public WebStarter(TomcatServletWebServerFactory tomcatServletWebServerFactory,
                      DispatcherServlet dispatcherServlet,
                      JsonExceptionHandler jsonExceptionHandler,
                      RestControllerContext restControllerContext) {
        this.servletWebServerFactory = tomcatServletWebServerFactory;
        this.dispatcherServlet = dispatcherServlet;
        this.jsonExceptionHandler = jsonExceptionHandler;
        this.restControllerContext = restControllerContext;
    }

    public void run(BringApplicationContext bringApplicationContext) {
        Map<String, List<RestControllerParams>> restControllerParams = restControllerContext.getParamsMap();

        webServer = (TomcatWebServer) servletWebServerFactory.getWebServer();
        Context context = servletWebServerFactory.getContext();
        context.getPipeline().addValve(jsonExceptionHandler);
        ServletContext servletContext = context.getServletContext();
        Tomcat tomcat = webServer.getTomcat();
        servletContext.setAttribute(BRING_CONTEXT, bringApplicationContext);
        servletContext.setAttribute(REST_CONTROLLER_PARAMS, restControllerParams);

        tomcat.addServlet(servletWebServerFactory.getContextPath(), DISPATCHER_SERVLET_NAME, dispatcherServlet);
        context.addServletMappingDecoded(URL_PATTERN, DISPATCHER_SERVLET_NAME);
    }

    public void stop() {
        webServer.stop();
    }
}
