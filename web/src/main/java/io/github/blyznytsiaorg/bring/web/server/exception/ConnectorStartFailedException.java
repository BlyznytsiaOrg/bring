package io.github.blyznytsiaorg.bring.web.server.exception;

/**
 * Exception thrown when a connector configured to listen on a specific port fails to start.
 *
 * <p>
 * Instances of this exception are typically thrown when an embedded web server, such as
 * Apache Tomcat, encounters an issue while attempting to start a connector on a specified port.
 * </p>
 *
 * @see WebServerException
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class ConnectorStartFailedException extends WebServerException {
    public ConnectorStartFailedException(int port) {
        super("Connector configured to listen on port " + port + " failed to start", null);
    }
}
