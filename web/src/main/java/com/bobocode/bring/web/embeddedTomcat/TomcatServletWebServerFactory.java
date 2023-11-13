package com.bobocode.bring.web.embeddedTomcat;

import com.bobocode.bring.web.server.WebServer;
import com.bobocode.bring.web.server.WebServerException;
import com.bobocode.bring.web.servlet.ServletWebServerFactory;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TomcatServletWebServerFactory implements ServletWebServerFactory {
    private int port = 8080;
    private String contextPath = "";
    private File baseDirectory;
    private Context context;

    public TomcatServletWebServerFactory() {
    }


    public TomcatServletWebServerFactory(int port) {
        this.port = port;
    }

    public TomcatServletWebServerFactory(int port, String contextPath) {
        this.port = port;
        this.contextPath = contextPath;
    }

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

    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public File getBaseDirectory() {
        return baseDirectory;
    }

    public int getPort() {
        return this.port;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public Context getContext() {
        return context;
    }

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
