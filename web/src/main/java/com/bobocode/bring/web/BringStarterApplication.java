package com.bobocode.bring.web;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.embeddedTomcat.TomcatServletWebServerFactory;
import com.bobocode.bring.web.embeddedTomcat.TomcatWebServer;
import com.bobocode.bring.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class BringStarterApplication {

    public static final String BRING_CONTEXT = "BRING_CONTEXT";
    public static void run(String basePackage) {
        BringApplicationContext bringApplicationContext = new BringApplicationContext(basePackage);

        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory(9090);
        TomcatWebServer webServer = (TomcatWebServer)factory.getWebServer();
        Context context = factory.getContext();
        var servletContext = context.getServletContext();
        Tomcat tomcat = webServer.getTomcat();
        servletContext.setAttribute(BRING_CONTEXT, bringApplicationContext);

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        tomcat.addServlet(factory.getContextPath(), "dispatcher", dispatcherServlet);
        context.addServletMappingDecoded("/", "dispatcher");

        context.getServletContext().getServletRegistrations().entrySet()
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }
}
