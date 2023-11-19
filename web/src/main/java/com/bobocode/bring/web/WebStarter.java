package com.bobocode.bring.web;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.server.TomcatServletWebServerFactory;
import com.bobocode.bring.web.server.TomcatWebServer;
import com.bobocode.bring.web.servlet.DispatcherServlet;
import com.bobocode.bring.web.servlet.JsonExceptionHandler;
import com.bobocode.bring.web.server.ServletWebServerFactory;
import jakarta.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

@Component
public class WebStarter {
    public static final String BRING_CONTEXT = "BRING_CONTEXT";
    public static final String DISPATCHER_SERVLET_NAME = "dispatcher";
    public static final String URL_PATTERN = "/";
    private final ServletWebServerFactory servletWebServerFactory;
    private final DispatcherServlet dispatcherServlet;
    private final JsonExceptionHandler jsonExceptionHandler;

    public WebStarter(TomcatServletWebServerFactory tomcatServletWebServerFactory,
                      DispatcherServlet dispatcherServlet,
                      JsonExceptionHandler jsonExceptionHandler) {
        this.servletWebServerFactory = tomcatServletWebServerFactory;
        this.dispatcherServlet = dispatcherServlet;
        this.jsonExceptionHandler = jsonExceptionHandler;
    }

    public void run(BringApplicationContext bringApplicationContext) {
        TomcatWebServer webServer = (TomcatWebServer)servletWebServerFactory.getWebServer();
        Context context = servletWebServerFactory.getContext();
        context.getPipeline().addValve(jsonExceptionHandler);
        ServletContext servletContext = context.getServletContext();
        Tomcat tomcat = webServer.getTomcat();
        servletContext.setAttribute(BRING_CONTEXT, bringApplicationContext);

        tomcat.addServlet(servletWebServerFactory.getContextPath(), DISPATCHER_SERVLET_NAME, dispatcherServlet);
        context.addServletMappingDecoded(URL_PATTERN, DISPATCHER_SERVLET_NAME);
    }
}
