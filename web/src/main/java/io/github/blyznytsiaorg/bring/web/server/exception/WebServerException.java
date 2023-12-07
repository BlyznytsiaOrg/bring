package io.github.blyznytsiaorg.bring.web.server.exception;

/**
 * RuntimeException used to represent generic exceptions related to web server operations.
 *
 * <p>
 * Instances of this exception can be thrown to indicate unexpected issues or errors
 * during the operation of an embedded web server, such as Apache Tomcat.
 * </p>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class WebServerException extends RuntimeException {

    public WebServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
