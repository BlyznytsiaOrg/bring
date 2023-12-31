package io.github.blyznytsiaorg.bring.web.server;

import io.github.blyznytsiaorg.bring.web.server.exception.ConnectorStartFailedException;
import io.github.blyznytsiaorg.bring.web.server.exception.WebServerException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link WebServer} interface for the Apache Tomcat server.
 * This class encapsulates the embedded Tomcat server and provides methods to start,
 * stop, and retrieve information about the server.
 * <p>
 * The {@code TomcatWebServer} constructor takes a configured {@link Tomcat} instance
 * and initializes the server. The server is started in a separate thread to trigger
 * initialization listeners. Additionally, a blocking non-daemon thread is created to
 * prevent immediate shutdown of the server.
 * </p>
 *
 * <p>
 * The {@code start} method starts the Tomcat server, and the {@code stop} method stops
 * the server. Both methods are synchronized to ensure thread safety during server
 * state changes.
 * </p>
 *
 * <p>
 * This class uses a monitor object for synchronization and a volatile boolean flag
 * ({@code started}) to track the server's state. It also contains methods for checking
 * whether connectors have started, getting a description of ports, and obtaining the
 * context path.
 * </p>
 *
 * @see WebServer
 * @see WebServerException
 * @see ConnectorStartFailedException
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */

@Slf4j
public class TomcatWebServer implements WebServer {
    @Getter
    private final Tomcat tomcat;
    private final Object monitor = new Object();
    private volatile boolean started;

    public TomcatWebServer(Tomcat tomcat) {
        this.tomcat = tomcat;
        initialize();
    }

    private void initialize() {
        synchronized (this.monitor) {
            try {
                // Start the server to trigger initialization listeners
                this.tomcat.start();

                // Unlike Jetty, all Tomcat threads are daemon threads. We create a
                // blocking non-daemon to stop immediate shutdown
                startDaemonAwaitThread();
                start();
            } catch (Exception ex) {
                stopSilently();
                destroySilently();
                throw new WebServerException("Unable to start embedded Tomcat", ex);
            }
        }
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread(() -> TomcatWebServer.this.tomcat.getServer().await());
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    private void stopSilently() {
        try {
            stopTomcat();
        } catch (LifecycleException ex) {
            // Ignore
        }
    }

    private void destroySilently() {
        try {
            this.tomcat.destroy();
        } catch (LifecycleException ex) {
            // Ignore
        }
    }

    @Override
    public void start() {
        synchronized (this.monitor) {
            if (this.started) {
                return;
            }
            checkThatConnectorsHaveStarted();
            this.started = true;
            log.info("Tomcat started on port(s): " + getPortsDescription(true) + " with context path '"
                    + getContextPath() + "'");
        }
    }

    private void checkThatConnectorsHaveStarted() {
        checkConnectorHasStarted(this.tomcat.getConnector());
        for (Connector connector : this.tomcat.getService().findConnectors()) {
            checkConnectorHasStarted(connector);
        }
    }

    private void checkConnectorHasStarted(Connector connector) {
        if (LifecycleState.FAILED.equals(connector.getState())) {
            throw new ConnectorStartFailedException(connector.getPort());
        }
    }

    private String getPortsDescription(boolean localPort) {
        StringBuilder ports = new StringBuilder();
        for (Connector connector : this.tomcat.getService().findConnectors()) {
            if (ports.length() != 0) {
                ports.append(' ');
            }
            int port = localPort ? connector.getLocalPort() : connector.getPort();
            ports.append(port).append(" (").append(connector.getScheme()).append(')');
        }
        return ports.toString();
    }

    private String getContextPath() {
        return Arrays.stream(this.tomcat.getHost().findChildren())
                .filter(Context.class::isInstance)
                .map(Context.class::cast)
                .map(Context::getPath)
                .collect(Collectors.joining(" "));
    }

    @Override
    public void stop() {
        synchronized (this.monitor) {
            try {
                this.started = false;
                try {
                    stopTomcat();
                    this.tomcat.destroy();
                }
                catch (LifecycleException ex) {
                    // swallow and continue
                }
            }
            catch (Exception ex) {
                throw new WebServerException("Unable to stop embedded Tomcat", ex);
            }
        }
    }

    private void stopTomcat() throws LifecycleException {
        this.tomcat.stop();
    }

    @Override
    public int getPort() {
        Connector connector = this.tomcat.getConnector();
        if (connector != null) {
            return connector.getLocalPort();
        }
        return -1;
    }
}
