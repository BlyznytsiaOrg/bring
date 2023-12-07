package io.github.blyznytsiaorg.bring.web.server;

import io.github.blyznytsiaorg.bring.web.server.exception.WebServerException;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * {@code ServletWebServerFactory} implementation for Apache Tomcat. This factory
 * creates and configures an embedded Tomcat web server instance.
 * <p>
 * The factory allows customization of the server's port, context path, base directory,
 * and other Tomcat-specific settings. It implements the {@link ServletWebServerFactory}
 * interface, providing a {@code getWebServer} method to create and return a
 * {@link TomcatWebServer}.
 * </p>
 *
 * @see ServletWebServerFactory
 * @see TomcatWebServer
 * @see WebServerException
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class TomcatServletWebServerFactory implements ServletWebServerFactory {
    @Getter
    private int port = 8080;
    private String contextPath = "";
    @Getter
    private File baseDirectory;
    private Context context;
    @Setter
    private LifecycleListener tomcatAppLifecycleListener;

    public TomcatServletWebServerFactory() {
    }


    public TomcatServletWebServerFactory(int port) {
        this.port = port;
    }

    public TomcatServletWebServerFactory(int port, String contextPath) {
        this.port = port;
        this.contextPath = contextPath;
    }

    /**
     * Returns a new instance of {@code TomcatWebServer} configured based on the
     * factory's settings.
     *
     * @return a configured instance of {@code TomcatWebServer}
     */
    @Override
    public WebServer getWebServer() {
        Tomcat tomcat = new Tomcat();
        File baseDir = (this.baseDirectory != null) ? this.baseDirectory : createTempDir("tomcat");
        String docBase = baseDir.getAbsolutePath();
        tomcat.setBaseDir(docBase);
        tomcat.setPort(port);
        context = tomcat.addWebapp(contextPath, docBase);
        return new TomcatWebServer(tomcat);
    }

    /**
     * Sets the base directory for the embedded Tomcat server.
     *
     * @param baseDirectory the base directory for the server to use
     */
    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Sets the port number for the embedded Tomcat server.
     *
     * @param port the port number for the server to use
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Returns the context path for the embedded Tomcat server.
     *
     * @return the context path (default is an empty string)
     */
    @Override
    public String getContextPath() {
        return contextPath;
    }

    /**
     * Returns the {@code Context} object associated with the embedded Tomcat server.
     *
     * @return the Tomcat {@code Context} object
     */
    @Override
    public Context getContext() {
        return context;
    }

    /**
     * Creates a temporary directory for Tomcat to use.
     *
     * @param prefix a prefix to be used in the name of the directory
     * @return a {@code File} representing the created temporary directory
     * @throws WebServerException if the temporary directory cannot be created
     */
    protected final File createTempDir(String prefix) {
        try {
            File tempDir = Files.createTempDirectory(prefix + "." + getPort() + ".").toFile();
            tempDir.deleteOnExit();
            return tempDir;
        } catch (IOException ex) {
            throw new WebServerException(
                    "Unable to create tempDir. java.io.tmpdir is set to " + System.getProperty("java.io.tmpdir"), ex);
        }
    }
}
