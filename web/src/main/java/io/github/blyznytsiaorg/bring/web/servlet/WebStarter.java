package io.github.blyznytsiaorg.bring.web.servlet;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.context.impl.BringApplicationContext;
import io.github.blyznytsiaorg.bring.web.server.ServletWebServerFactory;
import io.github.blyznytsiaorg.bring.web.server.TomcatServletWebServerFactory;
import io.github.blyznytsiaorg.bring.web.server.TomcatWebServer;
import io.github.blyznytsiaorg.bring.web.servlet.mapping.RestControllerParams;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import java.util.List;
import java.util.Map;

/**
 * The {@code WebStarter} class is responsible for initializing and running a web application
 * using Apache Tomcat as the embedded servlet container.
 *<p>
 * This class sets up the necessary components such as the servlet container, dispatcher servlet,
 * exception handler, and REST controller context. It also configures the servlet mappings and starts the web server.
 *</p>
 *
 * @see BringApplicationContext
 * @see ServletWebServerFactory
 * @see DispatcherServlet
 * @see JsonExceptionHandler
 * @see RestControllerContext
 * @see TomcatWebServer
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
@Component
public class WebStarter {

    /**
     * Attribute key for storing the BringApplicationContext in the servlet context.
     */
    public static final String BRING_CONTEXT = "BRING_CONTEXT";

    /**
     * Attribute key for storing the REST controller parameters map in the servlet context.
     */
    public static final String REST_CONTROLLER_PARAMS = "REST_CONTROLLER_PARAMS";

    /**
     * Default name for the dispatcher servlet.
     */
    public static final String DISPATCHER_SERVLET_NAME = "dispatcher";

    /**
     * Default URL pattern for the dispatcher servlet.
     */
    public static final String URL_PATTERN = "/";

    /**
     * The ServletWebServerFactory used to create the embedded servlet container.
     */
    private final ServletWebServerFactory servletWebServerFactory;

    /**
     * The DispatcherServlet responsible for handling HTTP requests.
     */
    private final DispatcherServlet dispatcherServlet;

    /**
     * The JsonExceptionHandler for handling JSON-related exceptions.
     */
    private final JsonExceptionHandler jsonExceptionHandler;

    /**
     * The RestControllerContext for managing REST controllers and their parameters.
     */
    private final RestControllerContext restControllerContext;

    /**
     * The TomcatWebServer instance representing the embedded Tomcat server.
     */
    private TomcatWebServer webServer;

    /**
     * Constructs a new WebStarter with the specified components.
     *
     * @param tomcatServletWebServerFactory The factory for creating the embedded Tomcat server.
     * @param dispatcherServlet            The DispatcherServlet for handling HTTP requests.
     * @param jsonExceptionHandler         The JsonExceptionHandler for handling JSON-related exceptions.
     * @param restControllerContext        The RestControllerContext for managing REST controllers and parameters.
     */
    public WebStarter(TomcatServletWebServerFactory tomcatServletWebServerFactory,
                      DispatcherServlet dispatcherServlet,
                      JsonExceptionHandler jsonExceptionHandler,
                      RestControllerContext restControllerContext) {
        this.servletWebServerFactory = tomcatServletWebServerFactory;
        this.dispatcherServlet = dispatcherServlet;
        this.jsonExceptionHandler = jsonExceptionHandler;
        this.restControllerContext = restControllerContext;
    }

    /**
     * Runs the web application.
     *
     * @param bringApplicationContext The BringApplicationContext for the application.
     */
    public void run(BringApplicationContext bringApplicationContext) {
        // Get REST controller parameters map
        Map<String, List<RestControllerParams>> restControllerParams = restControllerContext.getParamsMap();

        // Get Tomcat web server and context
        webServer = (TomcatWebServer) servletWebServerFactory.getWebServer();
        Context context = servletWebServerFactory.getContext();
        context.addLifecycleListener(new BringTomcatApplicationLifecycleListener(bringApplicationContext));

        // Add custom Lifecycle Listener
        log.debug("Adding {} to context...", BringTomcatApplicationLifecycleListener.class.getName());
        context.addLifecycleListener(new BringTomcatApplicationLifecycleListener(bringApplicationContext));

        // Add JsonExceptionHandler as a valve to the Tomcat pipeline
        log.debug("Adding JsonExceptionHandler {} as a valve to the Tomcat pipeline...", jsonExceptionHandler.getClass().getName());
        context.getPipeline().addValve(jsonExceptionHandler);

        // Get ServletContext and Tomcat instance
        ServletContext servletContext = context.getServletContext();
        Tomcat tomcat = webServer.getTomcat();

        // Set BringApplicationContext and REST controller parameters map as attributes in ServletContext
        log.debug("Setting BringApplicationContext as attribute in ServletContext...");
        servletContext.setAttribute(BRING_CONTEXT, bringApplicationContext);
        log.debug("Setting REST controller parameters map as attributes in ServletContext...");
        servletContext.setAttribute(REST_CONTROLLER_PARAMS, restControllerParams);

        // Register DispatcherServlet to Tomcat servlets and map it to the specified URL pattern
        tomcat.addServlet(servletWebServerFactory.getContextPath(), DISPATCHER_SERVLET_NAME, dispatcherServlet);
        context.addServletMappingDecoded(URL_PATTERN, DISPATCHER_SERVLET_NAME);
        log.debug("Registration of DispatcherServlet to Tomcat servlets and map it to the URL pattern was successfully completed");
    }

    /**
     * Stops the embedded Tomcat server.
     */
    public void stop() {
        webServer.stop();
    }
}
