package com.bobocode.bring.web.server;

public class ConnectorStartFailedException extends WebServerException {
    public ConnectorStartFailedException(int port) {
        super("Connector configured to listen on port " + port + " failed to start", null);
    }
}
