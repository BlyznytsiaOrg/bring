package com.bobocode.bring.web.server;

import org.apache.catalina.Context;

/**
 * A factory interface for creating and configuring servlet-based web servers.
 * Implementations of this interface provide methods to create a {@link WebServer}
 * instance and obtain information about the server's context and context path.
 * <p>
 * This interface is designed to be implemented by classes that create and configure
 * embedded servlet containers, such as Apache Tomcat.
 * </p>
 *
 * @see WebServer
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public interface ServletWebServerFactory {
    /**
     * Creates and returns a new instance of a servlet-based {@link WebServer}.
     *
     * @return a configured instance of {@link WebServer}
     * @see WebServer
     */
    WebServer getWebServer();

    /**
     * Returns the {@link Context} associated with the servlet-based web server.
     *
     * @return the server's {@link Context}
     * @see Context
     */
    Context getContext();

    /**
     * Returns the context path for the servlet-based web server.
     *
     * @return the context path
     */
    String getContextPath();
}
