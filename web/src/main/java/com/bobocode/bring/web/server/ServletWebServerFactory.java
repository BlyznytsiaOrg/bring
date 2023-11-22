package com.bobocode.bring.web.server;

import com.bobocode.bring.web.server.WebServer;
import org.apache.catalina.Context;

public interface ServletWebServerFactory {
    WebServer getWebServer();

    Context getContext();

    String getContextPath();
}
