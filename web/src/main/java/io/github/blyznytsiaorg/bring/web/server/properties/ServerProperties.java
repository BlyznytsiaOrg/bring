package io.github.blyznytsiaorg.bring.web.server.properties;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.annotation.Value;
import lombok.Data;

/**
 * Configuration properties for the web server.
 *
 * <p>
 * The values for these properties are typically defined in configuration files
 * and injected using the {@code @Value} annotation.
 * </p>
 *
 * <p>
 * Instances of this class are used to centralize configuration settings for the web
 * server within the application.
 * </p>
 *
 * @see Value
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */

@Data
@Component
public class ServerProperties {

    /**
     * The port on which the web server will listen for incoming requests.
     */
    @Value("server.port:9000")
    private int port;

    /**
     * The context path for the web server. It represents the base path for all
     * requests handled by the server.
     */
    @Value("server.contextPath:")
    private String contextPath;

    /**
     * Indicates whether stack traces should be included in error responses.
     * {@code true} if stack traces should be included, {@code false} otherwise
     */
    @Value("server.withStackTrace:false")
    private boolean withStackTrace;

    /**
     * Configuration property for controlling whether commit messages are included in GitInfo responses.
     * It is used by the DefaultActuatorService to determine whether to fetch and display commit messages.
     *
     * <p>The default value is {@code false}, meaning commit messages will not be included.
     */
    @Value("server.actuator.git.commit.withMessage:false")
    private boolean withMessage;

}
