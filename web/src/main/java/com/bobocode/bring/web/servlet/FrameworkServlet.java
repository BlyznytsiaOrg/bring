package com.bobocode.bring.web.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code FrameworkServlet} is an abstract class that extends {@code HttpServlet}
 * and provides a base implementation for handling HTTP requests in a framework.
 * Subclasses must implement the {@code processRequest} method to handle specific request processing.
 *<p>
 * This servlet initializes and processes various HTTP methods (GET, HEAD, POST, PUT, DELETE, OPTIONS, TRACE).
 * Each method is delegated to the common {@code processRequest} method.
 *</p>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
public abstract class FrameworkServlet extends HttpServlet {


    /**
     * Initializes the servlet with the provided configuration.
     *
     * @param config The servlet configuration.
     */
    @Override
    public void init(ServletConfig config) {
        log.info("Initializing Bring Servlet " + this.getClass().getSimpleName()
                + "'" + config.getServletName() + "'");
    }

    /**
     * Handles HTTP GET requests by delegating to the common {@code processRequest} method.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    /**
     * Handles HTTP HEAD requests by delegating to the common {@code processRequest} method.
     */
    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    /**
     * Handles HTTP POST requests by delegating to the common {@code processRequest} method.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    /**
     * Handles HTTP PUT requests by delegating to the common {@code processRequest} method.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    /**
     * Handles HTTP DELETE requests by delegating to the common {@code processRequest} method.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    /**
     * Handles HTTP OPTIONS requests by delegating to the common {@code processRequest} method.
     */
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    /**
     * Handles HTTP TRACE requests by delegating to the common {@code processRequest} method.
     */
    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    /**
     * Processes the HTTP request. Subclasses must implement this method to handle specific request processing.
     *
     * @param req  The HTTP request.
     * @param resp The HTTP response.
     */
    public abstract void processRequest(HttpServletRequest req, HttpServletResponse resp);
}
