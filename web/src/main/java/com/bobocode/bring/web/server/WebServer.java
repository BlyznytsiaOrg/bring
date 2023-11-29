package com.bobocode.bring.web.server;

import com.bobocode.bring.web.server.exception.WebServerException;

/**
 * The {@code WebServer} interface defines basic lifecycle methods to manage the
 * server's state, including starting and stopping. Additionally, it provides a method
 * to retrieve the port on which the server is listening.
 *
 * <p>
 * Implementations of this interface should follow the contract described in each
 * method's documentation. The {@code start} and {@code stop} methods are idempotent,
 * meaning calling them multiple times on the same server instance has no additional
 * effect once the server is in the desired state.
 * </p>
 *
 * <p>
 * If the server encounters any issues during the start or stop process, it throws
 * a {@code WebServerException} to indicate the problem.
 * </p>
 *
 * @see WebServerException
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */

public interface WebServer {

    /**
     * Starts the web server. Calling this method on an already started server has no
     * effect.
     */
    void start();

    /**
     * Stops the web server. Calling this method on an already stopped server has no
     * effect.
     */
    void stop();

    /**
     * Return the port this server is listening on.
     * @return the port (or -1 if none)
     */
    int getPort();
}
